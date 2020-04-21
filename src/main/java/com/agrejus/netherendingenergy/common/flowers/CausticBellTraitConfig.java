package com.agrejus.netherendingenergy.common.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.Ratio;

public class CausticBellTraitConfig {

    public static int getStageAdvanceTimeTicks(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return getStageAdvanceTimeTicks(1.2f);
            case AIMLESS:
                return getStageAdvanceTimeTicks(1.4f);
            case POLLINATING:
                return getStageAdvanceTimeTicks(1.6f);
            case ROAMING:
                return getStageAdvanceTimeTicks(1.8f);
            case ALTITUDINAL:
                return getStageAdvanceTimeTicks(1f);
            case DORMANT:
                return getStageAdvanceTimeTicks(2f);
            case NOXIOUS:
                return getStageAdvanceTimeTicks(.1f);
            case POTENT:
                return getStageAdvanceTimeTicks(.3f);
            case UNSTABLE:
                return getStageAdvanceTimeTicks(.6f);
            case STEADY:
                return getStageAdvanceTimeTicks(.8f);
            default:
                return Integer.MAX_VALUE;
        }
    }

    private static int getStageAdvanceTimeTicks(float minutes) {
        return Math.round(minutes * 60 * 20);
    }

    public static float getRandomYield(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return NetherEndingEnergy.roll(6, 8);
            case AIMLESS:
                return NetherEndingEnergy.roll(1, 6);
            case POLLINATING:
                return NetherEndingEnergy.roll(3, 5);
            case ROAMING:
                return 3f;
            case ALTITUDINAL:
                return NetherEndingEnergy.roll(1, 10);
            case DORMANT:
                return 1f;
            case NOXIOUS:
                return NetherEndingEnergy.roll(20, 30);
            case POTENT:
                return NetherEndingEnergy.roll(13, 18);
            case UNSTABLE:
                return NetherEndingEnergy.roll(10, 15);
            case STEADY:
                return 8f;
            default:
                return 0;
        }
    }

    public static float getRandomPurity(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return (float) NetherEndingEnergy.roll(70, 85) / (float) 100;
            case AIMLESS:
                return (float) NetherEndingEnergy.roll(10, 90) / (float) 100;
            case POLLINATING:
                return (float) NetherEndingEnergy.roll(95, 100) / (float) 100;
            case ROAMING:
                return (float) NetherEndingEnergy.roll(90, 100) / (float) 100;
            case NOXIOUS:
                return (float) NetherEndingEnergy.roll(20, 100) / (float) 100;
            case ALTITUDINAL:
            case POTENT:
                return (float) NetherEndingEnergy.roll(40, 100) / (float) 100;
            case UNSTABLE:
                return (float) NetherEndingEnergy.roll(50, 100) / (float) 100;
            case STEADY:
            case DORMANT:
            default:
                return 1f;
        }
    }

    public static float getpHLevel(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return 4f;
            case AIMLESS:
                return 4.5f;
            case POLLINATING:
                return 5f;
            case ROAMING:
                return 5.5f;
            case ALTITUDINAL:
                return 3.5f;
            case NOXIOUS:
                return 0f;
            case POTENT:
                return 2f;
            case UNSTABLE:
                return 2.5f;
            case STEADY:
                return 3f;
            case DORMANT:
            default:
                return 6f;
        }
    }

    public static float getRandomStrength(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return (float) NetherEndingEnergy.roll(90, 110) / (float) 100;
            case AIMLESS:
                return (float) NetherEndingEnergy.roll(10, 160) / (float) 100;
            case POLLINATING:
                return (float) NetherEndingEnergy.roll(80, 110) / (float) 100;
            case ROAMING:
                return (float) NetherEndingEnergy.roll(95, 110) / (float) 100;
            case ALTITUDINAL:
                return (float) NetherEndingEnergy.roll(80, 200) / (float) 100;
            case NOXIOUS:
                return (float) NetherEndingEnergy.roll(40, 500) / (float) 100;
            case POTENT:
                return (float) NetherEndingEnergy.roll(60, 200) / (float) 100;
            case UNSTABLE:
                return (float) NetherEndingEnergy.roll(30, 300) / (float) 100;
            case STEADY:
            case DORMANT:
            default:
                return 1f;
        }
    }

    public static float getMaxStrength(CausticBellTrait trait) {
        switch (trait) {
            case AIMLESS:
                return (float) 160/ (float) 100;
            case ADAPTOR:
            case POLLINATING:
            case NOXIOUS:
                return (float) 500 / (float) 100;
            case POTENT:
            case ALTITUDINAL:
                return (float) 200 / (float) 100;
            case UNSTABLE:
                return (float) 300 / (float) 100;
            case STEADY:
            case DORMANT:
            default:
                return 1f;
        }
    }

    public static float getMaxPurity(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return (float) 85 / (float) 100;
            case AIMLESS:
                return (float) 90/ (float) 100;
            case POLLINATING:
            case ROAMING:
            case NOXIOUS:
            case ALTITUDINAL:
            case POTENT:
            case UNSTABLE:
            case STEADY:
            case DORMANT:
            default:
                return (float) 100/ (float) 100;
        }
    }

    public static float getMaxYield(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return 10f;
            case AIMLESS:
                return 6f;
            case ROAMING:
                return 3f;
            case NOXIOUS:
                return 120f;
            case ALTITUDINAL:
                return 70f;
            case POTENT:
                return 80f;
            case UNSTABLE:
                return 100f;
            case STEADY:
            case POLLINATING:
                return 5f;
            case DORMANT:
            default:
                return 1f;
        }
    }

    public static int getAbsorbTicks(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return 2600;
            case AIMLESS:
                return 2800;
            case ROAMING:
                return 3200;
            case NOXIOUS:
                return 200;
            case ALTITUDINAL:
                return 2400;
            case POTENT:
                return 400;
            case UNSTABLE:
                return 1200;
            case STEADY:
                return 2000;
            case POLLINATING:
                return 3000;
            case DORMANT:
            default:
                return 5000;
        }
    }
}
