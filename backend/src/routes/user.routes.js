import express from "express";
import * as authRoutes from "../controller/auth.controller.js";
import { auth } from "../middleware/auth.middleware.js";

const authRouter = express();


authRouter.post("/register", authRoutes.register);
authRouter.post("/login", authRoutes.login);
authRouter.post("/logout", auth, authRoutes.logout);
authRouter.post("/refresh-token", authRoutes.refreshToken);
authRouter.post("/change-password", auth, authRoutes.changePassword);


export default authRouter;