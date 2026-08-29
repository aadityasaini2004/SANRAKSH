/**
 * Standalone migration script: Backfill sanrakshId for existing Elder users.
 *
 * Usage:
 *   npm run backfill:sanraksh
 *
 * Reads MONGO_URI from environment (or .env via dotenv).
 * Reuses the existing User model and backfillSanrakshIds utility.
 * No duplicate model compilation.
 */

import mongoose from "mongoose";
import dotenv from "dotenv";
import config from "../src/config/config.js";
import { backfillSanrakshIds } from "../src/utils/sanrakshId.js";

// Ensure config loads env vars (config.js already calls dotenv.config())
// but we import config to guarantee MONGO_URI is available.

async function run() {
    try {
        await mongoose.connect(config.MONGO_URI);
        console.log("Connected to MongoDB.");

        const updated = await backfillSanrakshIds();

        await mongoose.disconnect();
        console.log("Disconnected from MongoDB.");

        process.exit(0);
    } catch (error) {
        console.error("Backfill failed:", error);
        try {
            await mongoose.disconnect();
        } catch (_) {
            // ignore disconnect errors during cleanup
        }
        process.exit(1);
    }
}

run();
