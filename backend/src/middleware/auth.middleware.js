import userModel from "../model/user.model.js";
import config from "../config/config.js";
import jwt from "jsonwebtoken";

export async function auth(req, res, next) {
    try {
        const headers = req.headers.authorization;

        if(!headers) {
            return res.status(400).json({
                success: false,
                message: "Headers is missing!"
            })
        }

        const token = headers.split(" ")[1];
        
        if(!token) {
            return res.status(400).json({
                success: false,
                message: "Token is missing.",
            });
        }

        const decoded = jwt.verify(
            token,
            config.ACCESS_TOKEN_SECRET,
        );

        const user = await userModel.findById(decoded._id);
        
        if(!user){
            return res.status(401).json({
                success: false,
                message: "Access Denied!"
            });
        }

        req.user = user;
        next();

    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}