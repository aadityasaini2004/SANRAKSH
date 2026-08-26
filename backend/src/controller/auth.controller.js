import userModel from "../model/user.model.js";
import config from "../config/config.js";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";

export async function register(req, res) {
    try {
        const {name, email, password, phoneNumber, role} = req.body;

        if(!name || !email || !password || !phoneNumber || !role ) {
            return res.status(400).json({
                success: false,
                message: "All fields are required!"
            });
        }

        const isAlreadyUser = await userModel.findOne({email});

        if(isAlreadyUser) { 
            return res.status(409).json({
                success: false,
                message: "User already register with this email!"
            })
        }

        const hashPassword = await bcrypt.hash(password, 10);

        const user = await userModel.create({
            name: name,
            email: email,
            password: hashPassword,
            phoneNumber,
            role
        });

        return res.status(201).json({
            success: true,
            message: "User registered successful!",
            user: {
                _id: user._id,
                name: user.name,
                email: user.email,
                phoneNumber: user.phoneNumber,
                role: user.role,
            }
        })

    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}

export async function login(req, res) {
    try {
        const {email, password} = req.body;

        if(!email || !password) {
            return res.status(400).json({
                success: false,
                message: "All fields are required!"
            });
        }


        const user = await userModel.findOne({email});

        if(!user) {
            return res.status(401).json({
                success: false,
                message: "Invalid Email and Password",
            });
        }

        const verifyPassword = await bcrypt.compare(password, user.password);
        
        if(!verifyPassword) {
            return res.status(401).json({
                success: false,
                message: "Invalid Email and Password",
            });
        }

        const accessToken = jwt.sign({
            _id: user._id,

        }, config.ACCESS_TOKEN_SECRET, {
            expiresIn: "15m",
        });

        const refreshToken = jwt.sign({
            _id: user._id,
        }, config.REFRESH_TOKEN_SECRET, {
            expiresIn: "1d",
        });

        user.refreshToken = refreshToken;
        await user.save();

        return res.status(200).json({
            success: true,
            message: "User Login successful!",

            user: {
                _id: user._id,
                name: user.name,
                email: user.email,
                phoneNumber: user.phoneNumber,
                role: user.role,
            },

            token: {
                accessToken: accessToken,
                refreshToken: refreshToken
            }
        });

    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}

export async function refreshToken(req, res) {
    try {

        const {token} = req.body;

        if(!token) {
            return res.status(400).json({
                success: false,
                message: "Token is missing!"
            });
        }

        const decoded = jwt.verify(
            token,
            config.REFRESH_TOKEN_SECRET,
        );

        const user = await userModel.findById(decoded._id);

        if(token !== user.refreshToken) {
            return res.status(401).json({
                success: false,
                message: "Access Denied!",
            });
        }

        const newAccessToken = jwt.sign({
            _id: user._id,
        }, config.ACCESS_TOKEN_SECRET, {
            expiresIn: "15m"
        });

        return res.status(200).json({
            success: true,
            message: "new access token generated!",
            user: {
                _id: user._id,
                name: user.name,
                email: user.email,
                phoneNumber: user.phoneNumber,
                role: user.role,
            },
            token: {
                accessToken: newAccessToken,
            }
        })
        
        
    
    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}

export async function logout(req, res) {
    try {
        req.user.refreshToken = null;
        await req.user.save();

        return res.status(200).json({
            success: true,
            message: "Logout successful!"
        });

    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}

export async function changePassword(req, res) {
   try {
        let {currentPassword, newPassword, confirmPassword} = req.body;

        if(!currentPassword || !newPassword || !confirmPassword) {
            return res.status(400).json({
                success: false,
                message: "All fields are required!",
            });
        }

        const verifyPassword = await bcrypt.compare(currentPassword, req.user.password);

        if(!verifyPassword) {
            return res.status(401).json({
                success: false,
                message: "current password is not matched!"
            });
        }

        if(newPassword !== confirmPassword) {
            return res.status(401).json({
                success: false,
                message: "New Password and Current Password is not matched!"
            });
        }

        newPassword = await bcrypt.hash(newPassword, 10);
        req.user.password = newPassword;
        await req.user.save();

        return res.status(200).json({
            success: true,
            message: "Password reset successful!",
            user: {
                _id: req.user._id,
                email: req.user.email,
                name: req.user.name,
                role: req.user.role,
            }
        });

    } catch(error) {
        console.log("error", error);
        res.status(500).json({
            success: false,
            message: "something went wrong with server!"
        })
    }
}