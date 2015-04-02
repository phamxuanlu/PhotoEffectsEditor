package com.ss.photoeffectseditor.widget;

import android.content.Context;

import com.ss.photoeffectseditor.R;
import com.ss.photoeffectseditor.ip.BrightnessTransformation;
import com.ss.photoeffectseditor.ip.EffectCreator;
import com.ss.photoeffectseditor.ip.EffectTransformation;
import com.ss.photoeffectseditor.ip.GammaCorrectionTransformation;
import com.ss.photoeffectseditor.ip.GrayscaleTransformation;
import com.ss.photoeffectseditor.ip.HighlightTransformation;
import com.ss.photoeffectseditor.ip.HisEqualTransformation;
import com.ss.photoeffectseditor.ip.InvertTransformation;
import com.ss.photoeffectseditor.models.ToolObject;

/**
 * Created by phamxuanlu@gmail.com on 3/17/2015.
 */
public class ToolStructureBuilder {

    private Context context;

    public ToolStructureBuilder(Context context) {
        this.context = context;
    }

    public ToolObject enhance() {
        ToolObject enhance = new ToolObject();
        enhance.name = this.context.getResources().getString(R.string.enhance_tool_name);
        enhance.toolType = ToolObject.ToolType.TOOL_TYPE;
        enhance.iconResourceId = R.drawable.ic_btn_edit;


        ToolObject hisEqual = new ToolObject();
        hisEqual.name = "HD";
        hisEqual.toolType = ToolObject.ToolType.CONTENT_TYPE;
        hisEqual.transform = new HisEqualTransformation();
        enhance.addChild(hisEqual);

        return enhance;
    }

    public ToolObject effects() {
        ToolObject effect = new ToolObject();
        effect.name = context.getString(R.string.editor_tool_name_effects);
        effect.toolType = ToolObject.ToolType.TOOL_TYPE;
        effect.iconResourceId = R.drawable.ic_btn_edit;

        //Sepia
        ToolObject sepia = new ToolObject();
        sepia.name = "Sepia";
        sepia.toolType = ToolObject.ToolType.CONTENT_TYPE;
        float[] sp = new float[]{.393f, .769f, .189f, .349f, .686f, .168f, .272f, .534f, .131f};
        sepia.transform = new EffectTransformation(
                EffectCreator.extractEffect(sp, EffectCreator.ColorToneIntensity.BLUE, (short) 30));
        effect.addChild(sepia);

        //Metropolis
        ToolObject metropolis = new ToolObject();
        metropolis.name = "Metropolis";
        metropolis.toolType = ToolObject.ToolType.CONTENT_TYPE;
        metropolis.transform = new GrayscaleTransformation();
        effect.addChild(metropolis);

        //Invert
        ToolObject invert = new ToolObject();
        invert.name = "Negative";
        invert.toolType = ToolObject.ToolType.CONTENT_TYPE;
        invert.transform = new InvertTransformation();
        effect.addChild(invert);

        //Highlight
        ToolObject highlight = new ToolObject();
        highlight.name = "Highlight";
        highlight.toolType = ToolObject.ToolType.CONTENT_TYPE;
        highlight.transform = new HighlightTransformation();
        effect.addChild(highlight);

        return effect;
    }


    public ToolObject brightness() {

        //Brightness
        ToolObject brightness = new ToolObject();
        brightness.name = "Brightness";
        brightness.toolType = ToolObject.ToolType.OPTION_TYPE;
        brightness.transform = new BrightnessTransformation();
        brightness.iconResourceId = R.drawable.ic_btn_edit;

        return brightness;
    }

    public ToolObject gamma() {
        //Gamma Correction
        ToolObject gamma = new ToolObject();
        gamma.name = "Gamma";
        gamma.toolType = ToolObject.ToolType.OPTION_TYPE;
        gamma.transform = new GammaCorrectionTransformation(1.5f, 1.5f, 1.5f);
        gamma.iconResourceId = R.drawable.ic_btn_edit;

        return gamma;
    }

}
