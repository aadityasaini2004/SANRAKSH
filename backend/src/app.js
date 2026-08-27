import express from "express";
import morgan from "morgan";
import authRouter from "./routes/user.routes.js";
import safetyRoutes from "./routes/safety.routes.js";
import familyRoutes from "./routes/family.routes.js";

const app = express();

app.use(express.json());
app.use(morgan("dev"));

// application api end points
app.use("/api/auth/", authRouter);
app.use("/api/safety", safetyRoutes);
app.use("/api/family", familyRoutes);

export default app;