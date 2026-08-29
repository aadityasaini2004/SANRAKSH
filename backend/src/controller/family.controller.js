import mongoose from "mongoose";
import userModel from "../model/user.model.js";
import safetyEventModel from "../model/safety.mode.js";

// ── POST /api/family/link-elder ──
export async function linkElder(req, res) {
    try {
        let { sanrakshId } = req.body;

        // Validate sanrakshId is provided
        if (!sanrakshId) {
            return res.status(400).json({
                success: false,
                message: "Invalid or missing Sanraksh ID!"
            });
        }

        // Normalize to uppercase and trim
        sanrakshId = sanrakshId.trim().toUpperCase();

        // Find the target user by sanrakshId
        const elder = await userModel.findOne({ sanrakshId });

        if (!elder) {
            return res.status(404).json({
                success: false,
                message: "No elder found with this Sanraksh ID!"
            });
        }

        // Verify target user has elder role
        if (elder.role !== "elder") {
            return res.status(400).json({
                success: false,
                message: "Can only link elder accounts!"
            });
        }

        // Prevent linking self
        if (req.user._id.toString() === elder._id.toString()) {
            return res.status(400).json({
                success: false,
                message: "Cannot link your own account!"
            });
        }

        // Check for duplicate linking
        if (req.user.linkedElders && req.user.linkedElders.some(id => id.toString() === elder._id.toString())) {
            return res.status(409).json({
                success: false,
                message: "Elder is already linked!"
            });
        }

        // Add elder to linkedElders
        if (!req.user.linkedElders) {
            req.user.linkedElders = [];
        }
        req.user.linkedElders.push(elder._id);
        await req.user.save();

        return res.status(200).json({
            success: true,
            message: "Elder linked successfully!",
            elder: {
                _id: elder._id,
                name: elder.name,
                email: elder.email,
                phoneNumber: elder.phoneNumber,
                avatar: elder.avatar,
                role: elder.role,
                sanrakshId: elder.sanrakshId,
            }
        });

    } catch (error) {
        console.log("linkElder error:", error);
        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!"
        });
    }
}

// ── GET /api/family/elders ──
export async function getLinkedElders(req, res) {
    try {
        // Populate linkedElders with safe fields only
        const user = await userModel.findById(req.user._id)
            .populate("linkedElders", "name email phoneNumber avatar role sanrakshId");

        if (!user.linkedElders || user.linkedElders.length === 0) {
            return res.status(200).json({
                success: true,
                elders: []
            });
        }

        return res.status(200).json({
            success: true,
            elders: user.linkedElders
        });

    } catch (error) {
        console.log("getLinkedElders error:", error);
        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!"
        });
    }
}

// ── GET /api/safety/status/:elderId ──
export async function getElderStatus(req, res) {
    try {
        const { elderId } = req.params;

        // Validate elderId
        if (!elderId || !mongoose.Types.ObjectId.isValid(elderId)) {
            return res.status(400).json({
                success: false,
                message: "Invalid elderId!"
            });
        }

        // Check if elder is linked to this family user
        const familyUser = await userModel.findById(req.user._id);
        if (!familyUser.linkedElders || !familyUser.linkedElders.some(id => id.toString() === elderId)) {
            return res.status(403).json({
                success: false,
                message: "You are not authorized to view this elder's status!"
            });
        }

        // Get elder info
        const elder = await userModel.findById(elderId).select("name avatar role");
        if (!elder) {
            return res.status(404).json({
                success: false,
                message: "Elder not found!"
            });
        }

        // Get the latest safety event
        const latestEvent = await safetyEventModel.findOne({ userId: elderId })
            .sort({ createdAt: -1 })
            .select("type status createdAt");

        // Determine status
        let status = "NOT_CHECKED_IN";
        if (latestEvent) {
            status = latestEvent.status;
        }

        return res.status(200).json({
            success: true,
            status: status,
            elder: {
                _id: elder._id,
                name: elder.name,
                avatar: elder.avatar
            },
            lastEvent: latestEvent ? {
                _id: latestEvent._id,
                type: latestEvent.type,
                status: latestEvent.status,
                createdAt: latestEvent.createdAt
            } : null
        });

    } catch (error) {
        console.log("getElderStatus error:", error);
        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!"
        });
    }
}

// ── GET /api/safety/events/:elderId ──
export async function getElderEvents(req, res) {
    try {
        const { elderId } = req.params;

        // Validate elderId
        if (!elderId || !mongoose.Types.ObjectId.isValid(elderId)) {
            return res.status(400).json({
                success: false,
                message: "Invalid elderId!"
            });
        }

        // Check if elder is linked to this family user
        const familyUser = await userModel.findById(req.user._id);
        if (!familyUser.linkedElders || !familyUser.linkedElders.some(id => id.toString() === elderId)) {
            return res.status(403).json({
                success: false,
                message: "You are not authorized to view this elder's events!"
            });
        }

        // Get all safety events for this elder, sorted newest first
        const events = await safetyEventModel.find({ userId: elderId })
            .sort({ createdAt: -1 })
            .select("type status createdAt");

        return res.status(200).json({
            success: true,
            events: events
        });

    } catch (error) {
        console.log("getElderEvents error:", error);
        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!"
        });
    }
}
