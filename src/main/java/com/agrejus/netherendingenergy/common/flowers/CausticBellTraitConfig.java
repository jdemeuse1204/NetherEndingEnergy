package com.agrejus.netherendingenergy.common.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.models.NumberRange;

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

    public static NumberRange getYield(CausticBellTrait trait) {
        switch (trait) {
            case STEADY:
            case ADAPTOR:
                return new NumberRange(1, 8);
            case AIMLESS:
                return new NumberRange(1, 6);
            case POLLINATING:
                return new NumberRange(1, 5);
            case ROAMING:
                return new NumberRange(1, 3);
            case ALTITUDINAL:
                return new NumberRange(1, 10);
            case NOXIOUS:
                return new NumberRange(1, 35);
            case POTENT:
                return new NumberRange(1, 18);
            case UNSTABLE:
                return new NumberRange(1, 15);
            case DORMANT:
            default:
                return new NumberRange(1, 1);
        }
    }

    public static NumberRange getPurity(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return new NumberRange(.25f, .85f);
            case AIMLESS:
                return new NumberRange(.25f, .9f);
            case POLLINATING:
            case ROAMING:
            case NOXIOUS:
            case ALTITUDINAL:
            case POTENT:
            case UNSTABLE:
                return new NumberRange(.25f, 1f);
            case STEADY:
            case DORMANT:
            default:
                return new NumberRange(.25f, .1f);
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

    public static NumberRange getStrength(CausticBellTrait trait) {
        switch (trait) {
            case POLLINATING:
            case ROAMING:
            case ADAPTOR:
                return new NumberRange(.25f, 1.1f);
            case AIMLESS:
                return new NumberRange(.25f, 1.6f);
            case POTENT:
            case ALTITUDINAL:
                return new NumberRange(.25f, 2f);
            case NOXIOUS:
                return new NumberRange(.25f, 5f);
            case UNSTABLE:
                return new NumberRange(.25f, 3f);
            case STEADY:
            case DORMANT:
            default:
                return new NumberRange(.25f, .1f);
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
