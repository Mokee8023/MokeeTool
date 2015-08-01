package com.mokee.widget.Dialog;

import com.mokee.widget.Dialog.Effect.BaseEffects;
import com.mokee.widget.Dialog.Effect.FadeIn;
import com.mokee.widget.Dialog.Effect.Fall;
import com.mokee.widget.Dialog.Effect.FlipH;
import com.mokee.widget.Dialog.Effect.FlipV;
import com.mokee.widget.Dialog.Effect.NewsPaper;
import com.mokee.widget.Dialog.Effect.RotateBottom;
import com.mokee.widget.Dialog.Effect.RotateLeft;
import com.mokee.widget.Dialog.Effect.Shake;
import com.mokee.widget.Dialog.Effect.SideFall;
import com.mokee.widget.Dialog.Effect.SlideBottom;
import com.mokee.widget.Dialog.Effect.SlideLeft;
import com.mokee.widget.Dialog.Effect.SlideRight;
import com.mokee.widget.Dialog.Effect.SlideTop;
import com.mokee.widget.Dialog.Effect.Slit;

public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
