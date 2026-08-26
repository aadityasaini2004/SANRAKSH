import dotenv from "dotenv";
dotenv.config();

if(!process.env.PORT) {
    throw new Error("PORT is not defined!");
}

if(!process.env.MONGO_URI) {
    throw new Error("MONGO_URI is not defined!")
}

if(!process.env.ACCESS_TOKEN_SECRET) {
    throw new Error("ACCESS_TOKEN_SECRET is not defined!")
}

if(!process.env.REFRESH_TOKEN_SECRET) {
    throw new Error("REFRESH_TOKEN_SECRET is not defined!")
}




const config = {
    PORT: process.env.PORT,
    MONGO_URI: process.env.MONGO_URI,
    ACCESS_TOKEN_SECRET: process.env.ACCESS_TOKEN_SECRET,
    REFRESH_TOKEN_SECRET: process.env.REFRESH_TOKEN_SECRET,
}

export default config;