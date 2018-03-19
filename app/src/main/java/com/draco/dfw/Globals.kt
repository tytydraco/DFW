package com.draco.dfw

// default for OK is 0 so we must differentiate
val RESULT_OK = 1
val RESULT_NEUTRAL = 0
val RESULT_ERROR = -1

val EXTRA_SIZE = "size"
val EXTRA_DENSITY = "density"
val EXTRA_OVERSCAN = "overscan"
val EXTRA_TYPE = "type"
val EXTRA_NAMESPACE = "namespace"
val EXTRA_PUT = "put"
val EXTRA_GET = "get"
val EXTRA_VALUE = "value"
val EXTRA_STATUS = "status"
val EXTRA_GRANT = "grant"
val EXTRA_REVOKE = "revoke"

val IMMERSIVE_DEFAULT = "null"
val IMMERSIVE_NONE = "immersive.none=*"
val IMMERSIVE_NAV = "immersive.navigation=*"
val IMMERSIVE_STATUS = "immersive.status=*"
val IMMERSIVE_FULL = "immersive.full=*"

val NAMESPACE_GLOBAL = "global"
val NAMESPACE_SYSTEM = "system"
val NAMESPACE_SECURE = "secure"

val ANIMATION_WINDOW = "window"
val ANIMATION_TRANSITION = "transition"
val ANIMATION_ANIMATOR = "animator"
