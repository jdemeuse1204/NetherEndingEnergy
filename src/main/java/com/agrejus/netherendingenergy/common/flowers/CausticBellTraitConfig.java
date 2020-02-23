package com.agrejus.netherendingenergy.common.flowers;

import com.agrejus.netherendingenergy.common.Ratio;

public class CausticBellTraitConfig {
    public static int getDefaultYield(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
                return 7;
            case AIMLESS:
            case POLLINATING:
            case ROAMING:
                return 3;
            case ALTITUDINAL:
            case DORMANT:
                return 1;
            case NOXIOUS:
                return 20;
            case POTENT:
                return 15;
            case UNSTABLE:
                return 10;
            case STEADY:
                return 5;
            default:
                return 0;
        }
    }

    public static Ratio getDefaultPurity(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
            case AIMLESS:
            case POLLINATING:
            case ROAMING: ;
            case ALTITUDINAL:
            case DORMANT:
            case NOXIOUS:
            case POTENT:
            case UNSTABLE:
            case STEADY:
            default:
                return new Ratio(5,6);
        }
    }

    public static Ratio getDefaultStrength(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
            case AIMLESS:
            case POLLINATING:
            case ROAMING: ;
            case ALTITUDINAL:
            case DORMANT:
            case NOXIOUS:
            case POTENT:
            case UNSTABLE:
            case STEADY:
            default:
                return new Ratio(1,1);
        }
    }

    public static Ratio getDefaultBurnTimeAugment(CausticBellTrait trait) {
        switch (trait) {
            case ADAPTOR:
            case AIMLESS:
            case POLLINATING:
            case ROAMING: ;
            case ALTITUDINAL:
            case DORMANT:
            case NOXIOUS:
            case POTENT:
            case UNSTABLE:
            case STEADY:
            default:
                return new Ratio(1,1);
        }
    }
}
