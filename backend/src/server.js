import app from "./app.js";
import config from "./config/config.js";
import connectToDB from "./config/database.js";


async function serverStarter() {
    try {
        await connectToDB();
        app.listen(config.PORT, () => {
            console.log(`server is running on ${config.PORT}`)
        })
    } catch(error) {
        console.error("error", error);
    }
}

serverStarter();