package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

// UNIFIED HEADER ANIMATION ENGINE
// ------------------------------------------------------------------------------------------
// Controls how the toolbar header and the suggestion strip swap into each other while
// typing (see KeyboardComposeView). The style is user-selectable in the
// "Header Animation" settings screen; the value is persisted as the enum's name string.
// To add a new animation: add an entry to the enum and one `when` branch in toTransition().
// ------------------------------------------------------------------------------------------

enum class HeaderAnimation(val label: String, val description: String) {
    NONE("None", "Headers switch instantly, no animation"),
    FADE("Fade", "Smooth crossfade between the two headers"),
    SLIDE("Slide Up", "New header slides up from below"),
    SLIDE_FADE("Slide & Fade", "Slide up combined with a fade"),
    ZOOM("Zoom", "New header scales in from the center");

    companion object {
        fun fromName(name: String?): HeaderAnimation =
            entries.firstOrNull { it.name == name } ?: FADE
    }
}

@Composable
fun AnimatedHeaderSwitcher(
    showToolbar: Boolean,
    animation: HeaderAnimation,
    toolbar: @Composable () -> Unit,
    suggestions: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = showToolbar,
        transitionSpec = { animation.toTransition(this) },
        contentKey = { it }
    ) { toolbarShown ->
        if (toolbarShown) toolbar() else suggestions()
    }
}

private fun HeaderAnimation.toTransition(
    scope: AnimatedContentTransitionScope<Boolean>
): ContentTransform = when (this) {
    HeaderAnimation.NONE -> EnterTransition.None togetherWith ExitTransition.None

    HeaderAnimation.FADE -> fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))

    HeaderAnimation.SLIDE ->
        scope.slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(260)) togetherWith
            scope.slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(200))

    HeaderAnimation.SLIDE_FADE ->
        (scope.slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(260)) +
            fadeIn(animationSpec = tween(220))) togetherWith
            (scope.slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(200)) +
                fadeOut(animationSpec = tween(180)))

    HeaderAnimation.ZOOM ->
        (scaleIn(initialScale = 0.85f, animationSpec = tween(240)) +
            fadeIn(animationSpec = tween(200))) togetherWith
            (scaleOut(targetScale = 0.9f, animationSpec = tween(180)) +
                fadeOut(animationSpec = tween(160)))
}
