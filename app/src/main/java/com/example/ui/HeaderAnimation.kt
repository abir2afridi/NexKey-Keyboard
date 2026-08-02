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
import androidx.compose.ui.res.stringResource
import com.example.R

// UNIFIED HEADER ANIMATION ENGINE
// ------------------------------------------------------------------------------------------
// Controls how the toolbar header and the suggestion strip swap into each other while
// typing (see KeyboardComposeView). The style is user-selectable in the
// "Header Animation" settings screen; the value is persisted as the enum's name string.
// To add a new animation: add an entry to the enum and one `when` branch in toTransition().
// ------------------------------------------------------------------------------------------

enum class HeaderAnimation {
    NONE,
    FADE,
    SLIDE,
    SLIDE_FADE,
    ZOOM;

    companion object {
        fun fromName(name: String?): HeaderAnimation =
            entries.firstOrNull { it.name == name } ?: FADE
    }
}

@Composable
fun HeaderAnimation.labelText(): String = stringResource(
    when (this) {
        HeaderAnimation.NONE -> R.string.header_anim_none
        HeaderAnimation.FADE -> R.string.header_anim_fade
        HeaderAnimation.SLIDE -> R.string.header_anim_slide
        HeaderAnimation.SLIDE_FADE -> R.string.header_anim_slide_fade
        HeaderAnimation.ZOOM -> R.string.header_anim_zoom
    }
)

@Composable
fun HeaderAnimation.descriptionText(): String = stringResource(
    when (this) {
        HeaderAnimation.NONE -> R.string.header_anim_none_desc
        HeaderAnimation.FADE -> R.string.header_anim_fade_desc
        HeaderAnimation.SLIDE -> R.string.header_anim_slide_desc
        HeaderAnimation.SLIDE_FADE -> R.string.header_anim_slide_fade_desc
        HeaderAnimation.ZOOM -> R.string.header_anim_zoom_desc
    }
)

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
