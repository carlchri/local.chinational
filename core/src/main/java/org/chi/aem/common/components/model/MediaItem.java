/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.applet.AudioClip;

public class MediaItem {
    private String type;
    private Video video;
    private Audio audio;

    public MediaItem(String type, Video video, Audio audio) {
        this.type = type;
        this.video = video;
        this.audio = audio;
    }

    public String getType() {
        return type;
    }

    public Video getVideo() {
        return video;
    }

    public Audio getAudio() {
        return audio;
    }
}