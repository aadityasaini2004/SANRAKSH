import express from "express";

import * as safetyRoutes from "../controller/safety.controller.js";

import { auth } from "../middleware/auth.middleware.js";

const router = express.Router();

router.post("/check-in", auth, safetyRoutes.checkIn);

router.post("/sos", auth, safetyRoutes.triggerSOS);

export default router;