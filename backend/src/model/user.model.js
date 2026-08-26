import mongoose from "mongoose";


const userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: [true, "name is required!"],
        trim: true,
    },

    email: {
        type: String,
        required: [true, "email is required!"],
        unique: true,
        lowercase: true,
    },

    password: {
        type: String,
        required: [true, "password is required!"],
        minlength: [8, "Password must be at least 8 characters!"]
    },

    phoneNumber: {
        type: String,
        required: true,
        match: [/^[6-9]\d{9}$/, "Please enter a valid 10 digit phone number!"],
        trim: true,
    },

    avatar: {
        type: String,
        trim: true,
    },

    role: {
        type: String,
        enum: ["elder", "family"],
        required: true,
    },

    refreshToken: {
        type: String,
        default: null,
    }


}, {timestamps: true});

const userModel = mongoose.model("User", userSchema);

export default userModel;