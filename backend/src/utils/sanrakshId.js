import userModel from "../model/user.model.js";

// Characters that are unambiguous (no 0/O, 1/I/L)
const CHARS = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

/**
 * Generate a random Sanraksh ID in the format SNR-XXXXXX
 * Uses characters that are visually distinct to reduce transcription errors.
 * Retries on collision (up to 10 attempts).
 */
export async function generateSanrakshId() {
    const MAX_RETRIES = 10;

    for (let attempt = 0; attempt < MAX_RETRIES; attempt++) {
        let id = "SNR-";

        for (let i = 0; i < 6; i++) {
            const idx = Math.floor(Math.random() * CHARS.length);
            id += CHARS[idx];
        }

        const exists = await userModel.exists({ sanrakshId: id });

        if (!exists) {
            return id;
        }
    }

    throw new Error("Failed to generate unique Sanraksh ID after multiple attempts");
}

/**
 * Backfill sanrakshId for existing Elder users who are missing one.
 * Safe, idempotent, and race-aware:
 *  - Only targets role === "elder" with missing/empty sanrakshId.
 *  - Never overwrites an existing sanrakshId.
 *  - Uses the unique index + retry for collision safety.
 *
 * @returns {Promise<number>} Number of Elder users updated.
 */
export async function backfillSanrakshIds() {
    console.log("Sanraksh ID backfill started...");

    const elders = await userModel.find({
        role: "elder",
        $or: [
            { sanrakshId: { $exists: false } },
            { sanrakshId: null },
            { sanrakshId: "" },
        ],
    });

    if (elders.length === 0) {
        console.log("No elder users require backfill.");
        console.log("Sanraksh ID backfill completed.");
        return 0;
    }

    console.log(`Found ${elders.length} elder user(s) without Sanraksh ID.`);

    let updated = 0;

    for (const elder of elders) {
        try {
            const sanrakshId = await generateSanrakshId();
            await userModel.updateOne(
                { _id: elder._id, sanrakshId: { $in: [null, ""] } },
                { $set: { sanrakshId } }
            );
            updated++;
        } catch (err) {
            // If the update fails (e.g. another process assigned an ID first),
            // log and continue — do not crash the entire startup.
            console.error(`  Failed to assign Sanraksh ID to ${elder.email}:`, err.message);
        }
    }

    console.log(`Assigned Sanraksh ID to ${updated} elder user(s).`);
    console.log("Sanraksh ID backfill completed.");
    return updated;
}
