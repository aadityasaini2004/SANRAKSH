import safetyEventModel from "../model/safety.mode.js";

export async function checkIn(req, res) {
    try {
        const userId = req.user._id;

        const safetyEvent = await safetyEventModel.create({
            userId,
            type: "CHECK_IN",
            status: "SAFE",
        });

        return res.status(201).json({
            success: true,
            message: "Safety check-in recorded successfully!",
            event: {
                _id: safetyEvent._id,
                type: safetyEvent.type,
                status: safetyEvent.status,
                createdAt: safetyEvent.createdAt,
            },
        });
    } catch (error) {
        console.log("checkIn error:", error);

        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!",
        });
    }
}

export async function triggerSOS(req, res) {
    try {
        const userId = req.user._id;

        const safetyEvent = await safetyEventModel.create({
            userId,
            type: "SOS",
            status: "EMERGENCY",
        });

        return res.status(201).json({
            success: true,
            message: "Emergency SOS alert created successfully!",
            event: {
                _id: safetyEvent._id,
                type: safetyEvent.type,
                status: safetyEvent.status,
                createdAt: safetyEvent.createdAt,
            },
        });
    } catch (error) {
        console.log("triggerSOS error:", error);

        return res.status(500).json({
            success: false,
            message: "Something went wrong with server!",
        });
    }
}