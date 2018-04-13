package com.junyou.io.global;

import java.io.Serializable;

public interface ChannelId extends Serializable, Comparable<ChannelId> {

	String asShortText();

	String asLongText();
}
