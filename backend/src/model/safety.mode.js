import mongoose from "mongoose";

const safetyEventSchema = new mongoose.Schema(
    {
        userId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: [true, "User is required!"],
        },

        type: {
            type: String,
            enum: ["CHECK_IN", "SOS"],
            required: [true, "Event type is required!"],
        },

        status: {
            type: String,
            enum: ["SAFE", "EMERGENCY"],
            required: [true, "Event status is required!"],
        },
    },
    {
        timestamps: true,
    }
);

const safetyEventModel = mongoose.model("SafetyEvent", safetyEventSchema);

export default safetyEventModel;