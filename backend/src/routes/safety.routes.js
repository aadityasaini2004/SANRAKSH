import express from "express";

import * as safetyRoutes from "../controller/safety.controller.js";
import * as familyController from "../controller/family.controller.js";

import { auth } from "../middleware/auth.middleware.js";

const router = express.Router();

// Elder safety endpoints
router.post("/check-in", auth, safetyRoutes.checkIn);
router.post("/sos", auth, safetyRoutes.triggerSOS);

// Family safety query endpoints
router.get("/status/:elderId", auth, familyController.getElderStatus);
router.get("/events/:elderId", auth, familyController.getElderEvents);

export default router;