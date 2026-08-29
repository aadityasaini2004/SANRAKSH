import app from "./app.js";
import config from "./config/config.js";
import connectToDB from "./config/database.js";
import { backfillSanrakshIds } from "./utils/sanrakshId.js";


async function serverStarter() {
    try {
        await connectToDB();
        await backfillSanrakshIds();
        app.listen(config.PORT, () => {
            console.log(`server is running on ${config.PORT}`)
        })
    } catch(error) {
        console.error("Startup error:", error);
        process.exit(1);
    }
}

serverStarter();