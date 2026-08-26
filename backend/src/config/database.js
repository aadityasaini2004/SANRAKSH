import mongoose from "mongoose";
import config from "./config.js";

async function connectToDB() {
    try {
        await mongoose.connect(config.MONGO_URI);
        console.log("mongodb is connected!");
    } catch(error) {
        console.log("mongodb is not connected.", error);
        process.exit(1);
    }
}

export default connectToDB;