import express from "express";
import morgan from "morgan";
import authRouter from "./routes/user.routes.js";

const app = express();

app.use(express.json());
app.use(morgan("dev"));

// application api end points
app.use("/api/auth/", authRouter);

export default app;