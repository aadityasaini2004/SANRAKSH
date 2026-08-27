import express from "express";
import * as familyController from "../controller/family.controller.js";
import { auth } from "../middleware/auth.middleware.js";

const router = express.Router();

// All family routes require authentication
router.use(auth);

// POST /api/family/link-elder - Link an elder to family account
router.post("/link-elder", familyController.linkElder);

// GET /api/family/elders - Get all linked elders
router.get("/elders", familyController.getLinkedElders);

export default router;
