package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.KeyboardTheme

// ─────────────────────────────────────────────────────────────────────────────
// All emoji categories — complete Unicode emoji list
// ─────────────────────────────────────────────────────────────────────────────

private data class EmojiCategory(val icon: ImageVector, val name: String, val emojis: List<String>)

private val ALL_EMOJI_CATEGORIES = listOf(

    EmojiCategory(Icons.Filled.DateRange, "Recent", emptyList()),

    EmojiCategory(Icons.Filled.Star, "Smileys", listOf(
        "😀","😃","😄","😁","😆","😅","😂","🤣","🥲","☺️","😊","😇",
        "🙂","🙃","😉","😌","😍","🥰","😘","😗","😙","😚","😋","😛",
        "😝","😜","🤪","🤨","🧐","🤓","😎","🥸","🤩","🥳","😏","😒",
        "😞","😔","😟","😕","🙁","☹️","😣","😖","😫","😩","🥺","😢",
        "😭","😤","😠","😡","🤬","🤯","😳","🥵","🥶","😱","😨","😰",
        "😥","😓","🤗","🤔","🤭","🤫","🤥","😶","😐","😑","😬","🙄",
        "😯","😦","😧","😮","😲","🥱","😴","🤤","😪","😵","🤐","🥴",
        "🤢","🤮","🤧","😷","🤒","🤕","🤑","🤠","😈","👿","💀","☠️",
        "💩","🤡","👹","👺","👻","👽","👾","🤖","😺","😸","😹","😻",
        "😼","😽","🙀","😿","😾","🙈","🙉","🙊"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.People, "People", listOf(
        "👋","🤚","🖐️","✋","🖖","👌","🤌","🤏","✌️","🤞","🤟","🤘",
        "🤙","👈","👉","👆","🖕","👇","☝️","👍","👎","✊","👊","🤛",
        "🤜","👏","🙌","👐","🤲","🤝","🙏","✍️","💅","🤳","💪","🦾",
        "🦿","🦵","🦶","👂","🦻","👃","🫀","🫁","🧠","🦷","🦴","👀",
        "👁️","👅","👄","💋","👶","🧒","👦","👧","🧑","👱","👨","🧔",
        "👩","🧓","👴","👵","🙍","🙎","🙅","🙆","💁","🙋","🧏","🙇",
        "🤦","🤷","👮","🕵️","💂","👷","🫅","🤴","👸","👳","👲","🧕",
        "🤵","👰","🤰","🫃","🤱","👼","🎅","🤶","🦸","🦹","🧙","🧝",
        "🧛","🧟","🧞","🧜","🧚","🧑‍🤝‍🧑","👫","👬","👭","💏","💑","👨‍👩‍👦",
        "👨‍👩‍👧","👨‍👩‍👧‍👦","👨‍👩‍👦‍👦","👨‍👩‍👧‍👧","🏃","🧍","🧎","🧖","🧗","🤸","⛹️","🤺",
        "🏌️","🏇","🧘","🏋️","🤼","🤾","🤹","🧑‍🦯","🧑‍🦼","🧑‍🦽"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Favorite, "Hearts", listOf(
        "❤️","🧡","💛","💚","💙","💜","🖤","🤍","🤎","❤️‍🔥","❤️‍🩹",
        "💔","❣️","💕","💞","💓","💗","💖","💘","💝","💟","♥️","💌",
        "💜","🫶","💝","💖","💗","💓","💞","💕","❤","🩷","🩵","🩶"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Pets, "Animals", listOf(
        "🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼","🐨","🐯","🦁","🐮",
        "🐷","🐽","🐸","🐵","🙈","🙉","🙊","🐒","🐔","🐧","🐦","🐤",
        "🦆","🦅","🦉","🦇","🐺","🐗","🐴","🦄","🐝","🪱","🐛","🦋",
        "🐌","🐞","🐜","🪰","🪲","🦟","🦗","🪳","🕷️","🦂","🐢","🐍",
        "🦎","🦖","🦕","🐙","🦑","🦐","🦞","🦀","🐡","🐠","🐟","🐬",
        "🐳","🐋","🦈","🐊","🐅","🐆","🦓","🦍","🦧","🦣","🐘","🦛",
        "🦏","🐪","🐫","🦒","🦘","🦬","🐃","🐂","🐄","🫎","🐎","🐖",
        "🐏","🐑","🦙","🐐","🦌","🐕","🐩","🦮","🐕‍🦺","🐈","🐈‍⬛","🪶",
        "🐓","🦃","🦤","🦚","🦜","🦢","🪽","🦩","🕊️","🐇","🦝","🦨",
        "🦡","🦫","🦦","🦥","🐁","🐀","🐿️","🦔","🐾","🌸","🌺","🌻",
        "🌹","🌷","🌼","🌾","🍀","🌿","🍃","🍂","🍁","🌲","🌳","🌴",
        "🎋","🎍","☘️","🪴","🌵","🌊","🌙","⭐","🌟","💫","✨","🔥",
        "🌈","☀️","⛅","🌤️","🌥️","☁️","🌦️","⛈️","🌩️","🌨️","❄️","🌬️"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Restaurant, "Food", listOf(
        "🍏","🍎","🍐","🍊","🍋","🍋‍🟩","🍌","🍉","🍇","🍓","🫐","🍈",
        "🍒","🍑","🥭","🍍","🥥","🥝","🍅","🍆","🥑","🥦","🥬","🥒",
        "🌶️","🫑","🌽","🥕","🫛","🧄","🧅","🥔","🍠","🫚","🧆","🥚",
        "🍳","🧈","🥞","🧇","🥓","🥩","🍗","🍖","🌭","🍔","🍟","🍕",
        "🫓","🥪","🥙","🧆","🌮","🌯","🫔","🥗","🥘","🫕","🥫","🫙",
        "🍱","🍘","🍙","🍚","🍛","🍜","🍝","🍠","🍢","🍣","🍤","🍥",
        "🥮","🍡","🥟","🦪","🍦","🍧","🍨","🍩","🍪","🎂","🍰","🧁",
        "🥧","🍫","🍬","🍭","🍮","🍯","🍼","🥛","☕","🫖","🍵","🧃",
        "🥤","🧋","🍶","🍺","🍻","🥂","🍷","🥃","🍸","🍹","🧉","🍾",
        "🧊","🥄","🍴","🍽️","🥣","🥗","🧂","🫙"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.EmojiEvents, "Activity", listOf(
        "⚽","🏀","🏈","⚾","🥎","🎾","🏐","🏉","🥏","🎱","🪀","🏓",
        "🏸","🏒","🏑","🥍","🏏","🪃","🥅","⛳","🪁","🤿","🎽","🎿",
        "🛷","🥌","🎯","🪃","🎱","🎳","🎰","🎲","♟️","🧩","🪅","🪆",
        "🃏","🀄","🎭","🖼️","🎨","🧵","🪡","🧶","🪢","🥊","🥋","🥅",
        "🏆","🥇","🥈","🥉","🏅","🎖️","🎗️","🎫","🎟️","🎪","🤹","🎠",
        "🎡","🎢","🎬","🎤","🎧","🎼","🎹","🥁","🪘","🎷","🎺","🎸",
        "🪕","🎻","🎲","🤺","🏇","🧗","🤸","⛹️","🤾","🏌️","🏋️","🤼",
        "🤺","🤼","🤸","🏊","🏄","🚣","🧘","🧜","🏂","🪂","🏋️","🤸"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Flight, "Travel", listOf(
        "🚗","🚕","🚙","🚌","🚎","🏎️","🚓","🚑","🚒","🚐","🛻","🚚",
        "🚛","🚜","🏍️","🛵","🛺","🚲","🛴","🛹","🛼","🚏","🛣️","🛤️",
        "⛽","🚨","🚥","🚦","🛑","🚧","⚓","🛟","⛵","🚤","🛥️","🛳️",
        "⛴️","🚢","✈️","🛩️","🛫","🛬","🪂","💺","🚁","🚟","🚠","🚡",
        "🛰️","🚀","🛸","🎆","🎇","🗺️","🗾","🧭","🏔️","⛰️","🌋","🗻",
        "🏕️","🏖️","🏜️","🏝️","🏞️","🏟️","🏛️","🏗️","🧱","🪨","🪵","🛖",
        "🏘️","🏚️","🏠","🏡","🏢","🏣","🏤","🏥","🏦","🏨","🏩","🏪",
        "🏫","🏬","🏭","🏯","🏰","💒","🗼","🗽","⛪","🕌","🛕","🕍",
        "⛩️","🕋","⛲","⛺","🌁","🌃","🏙️","🌄","🌅","🌆","🌇","🌉",
        "🎠","🎡","🎢","💈","🎪","🚂","🚃","🚄","🚅","🚆","🚇","🚈",
        "🚉","🚊","🚝","🚞","🚋","🚌","🚍","🚎","🚐","🚑","🚒","🚓",
        "🚔","🚕","🚖","🚗","🚘","🚙","🛻","🚚","🚛","🚜","🏎️","🏍️"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Lightbulb, "Objects", listOf(
        "⌚","📱","📲","💻","⌨️","🖥️","🖨️","🖱️","🖲️","💾","💿","📀",
        "🧮","📷","📸","📹","🎥","📽️","🎞️","📞","☎️","📟","📠","📺",
        "📻","🧭","⏱️","⏲️","⏰","🕰️","⌛","⏳","📡","🔋","🪫","🔌",
        "💡","🔦","🕯️","🪔","🧯","🛢️","💸","💵","💴","💶","💷","🪙",
        "💰","💳","💎","⚖️","🪜","🧰","🪛","🔧","🔨","⚒️","🛠️","⛏️",
        "🪚","🔩","🪤","🧲","🪝","🔑","🗝️","🔐","🔏","🔒","🔓","🪪",
        "📦","📫","📪","📬","📭","📮","🗳️","✏️","✒️","🖊️","🖋️","📝",
        "📁","📂","🗂️","📅","📆","🗒️","🗓️","📇","📈","📉","📊","📋",
        "📌","📍","✂️","🖇️","📎","🗃️","🗄️","🗑️","🔍","🔎","🔏","🔐",
        "🔒","🔓","🔔","🔕","📣","📢","🔈","🔇","📯","🔔","🔕","🎵",
        "🎶","📻","🎷","🪗","🎸","🎹","🥁","🪘","🎺","🎻","🪕","🎤",
        "🎧","📻","🎙️","📻","🧸","🪆","🖼️","🧵","🪡","🧶","🪢","👓",
        "🕶️","🥽","🌂","☂️","🧵","🧶","🪡","🧣","🧤","🧥","🥻","👘"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Category, "Symbols", listOf(
        "🔴","🟠","🟡","🟢","🔵","🟣","🟤","⚫","⚪","🔶","🔷","🔸",
        "🔹","🔺","🔻","💠","🔘","🔲","🔳","⬛","⬜","▪️","▫️","◾",
        "◽","◼️","◻️","🟥","🟧","🟨","🟩","🟦","🟪","🟫","⏏️","▶️",
        "⏩","⏭️","⏯️","◀️","⏪","⏮️","🔼","⏫","🔽","⏬","⏹️","⏺️",
        "⏸️","🔁","🔂","🔀","🔃","🎦","🔅","🔆","📶","📳","📴","📵",
        "📳","🔇","🔈","🔉","🔊","📢","📣","🔔","🔕","🎵","🎶","💯",
        "✅","❎","🔴","⭕","❌","❗","❕","❓","❔","🚫","⛔","🚳","🚭",
        "🚯","🚱","🚷","📵","🔞","☢️","☣️","⬆️","↗️","➡️","↘️","⬇️",
        "↙️","⬅️","↖️","↕️","↔️","↩️","↪️","⤴️","⤵️","🔄","🔙","🔛",
        "🔝","🔜","ℹ️","🔤","🔡","🔠","🆒","🆓","🆕","🆖","🆗","🆙",
        "🆚","🈴","🈵","🈹","🈲","🅰️","🅱️","🆎","🆑","🅾️","🆘","🚾",
        "⚠️","🛗","♻️","✔️","🔱","📛","🔰","⭕","✅","☑️","💲","©️","®️","™️",
        "#️⃣","*️⃣","0️⃣","1️⃣","2️⃣","3️⃣","4️⃣","5️⃣","6️⃣","7️⃣","8️⃣","9️⃣",
        "🔟","🔠","🔡","🔢","🔣","🔤","▶️","⏸️","⏹️","⏺️","⏭️","⏮️",
        "♈","♉","♊","♋","♌","♍","♎","♏","♐","♑","♒","♓","⛎",
        "🏧","🔱","⚜️","♿","🚹","🚺","🚼","🚻","🚽","🚿","🛁","🧹"
    )),

    EmojiCategory(androidx.compose.material.icons.Icons.Filled.Flag, "Flags", listOf(
        "🏳️","🏴","🏴‍☠️","🚩","🎌","🏁","🏳️‍🌈","🏳️‍⚧️",
        "🇦🇫","🇦🇱","🇩🇿","🇦🇩","🇦🇴","🇦🇮","🇦🇬","🇦🇷","🇦🇲","🇦🇼","🇦🇺","🇦🇹",
        "🇦🇿","🇧🇸","🇧🇭","🇧🇩","🇧🇧","🇧🇾","🇧🇪","🇧🇿","🇧🇯","🇧🇲","🇧🇹","🇧🇴",
        "🇧🇦","🇧🇼","🇧🇷","🇧🇳","🇧🇬","🇧🇫","🇧🇮","🇨🇻","🇰🇭","🇨🇲","🇨🇦","🇰🇾",
        "🇨🇫","🇹🇩","🇨🇱","🇨🇳","🇨🇴","🇰🇲","🇨🇩","🇨🇬","🇨🇷","🇭🇷","🇨🇺","🇨🇼",
        "🇨🇾","🇨🇿","🇩🇰","🇩🇯","🇩🇲","🇩🇴","🇪🇨","🇪🇬","🇸🇻","🇬🇶","🇪🇷","🇪🇪",
        "🇸🇿","🇪🇹","🇫🇯","🇫🇮","🇫🇷","🇬🇦","🇬🇲","🇬🇪","🇩🇪","🇬🇭","🇬🇮","🇬🇷",
        "🇬🇩","🇬🇹","🇬🇳","🇬🇼","🇬🇾","🇭🇹","🇭🇳","🇭🇰","🇭🇺","🇮🇸","🇮🇳","🇮🇩",
        "🇮🇷","🇮🇶","🇮🇪","🇮🇱","🇮🇹","🇯🇲","🇯🇵","🇯🇴","🇰🇿","🇰🇪","🇰🇮","🇽🇰",
        "🇰🇼","🇰🇬","🇱🇦","🇱🇻","🇱🇧","🇱🇸","🇱🇷","🇱🇾","🇱🇮","🇱🇹","🇱🇺","🇲🇴",
        "🇲🇬","🇲🇼","🇲🇾","🇲🇻","🇲🇱","🇲🇹","🇲🇭","🇲🇷","🇲🇺","🇲🇽","🇫🇲","🇲🇩",
        "🇲🇨","🇲🇳","🇲🇪","🇲🇸","🇲🇦","🇲🇿","🇲🇲","🇳🇦","🇳🇷","🇳🇵","🇳🇱","🇳🇿",
        "🇳🇮","🇳🇪","🇳🇬","🇳🇴","🇴🇲","🇵🇰","🇵🇼","🇵🇸","🇵🇦","🇵🇬","🇵🇾","🇵🇪",
        "🇵🇭","🇵🇱","🇵🇹","🇵🇷","🇶🇦","🇷🇴","🇷🇺","🇷🇼","🇰🇳","🇱🇨","🇻🇨","🇼🇸",
        "🇸🇲","🇸🇹","🇸🇦","🇸🇳","🇷🇸","🇸🇨","🇸🇱","🇸🇬","🇸🇽","🇸🇰","🇸🇮","🇸🇧",
        "🇸🇴","🇿🇦","🇸🇸","🇪🇸","🇱🇰","🇸🇩","🇸🇷","🇸🇪","🇨🇭","🇸🇾","🇹🇼","🇹🇯",
        "🇹🇿","🇹🇭","🇹🇱","🇹🇬","🇹🇴","🇹🇹","🇹🇳","🇹🇷","🇹🇲","🇺🇬","🇺🇦","🇦🇪",
        "🇬🇧","🏴󠁧󠁢󠁥󠁮󠁧󠁿","🏴󠁧󠁢󠁳󠁣󠁴󠁿","🏴󠁧󠁢󠁷󠁬󠁳󠁿","🇺🇸","🇺🇾","🇺🇿","🇻🇺","🇻🇪","🇻🇳","🇾🇪","🇿🇲","🇿🇼"
    ))
)

// ─────────────────────────────────────────────────────────────────────────────
// EmojiPanel Composable
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EmojiPanel(theme: KeyboardTheme, onEmojiClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf(1) } // default: Smileys
    val recentEmojis = remember { mutableStateListOf<String>() }

    // Build live category list (inject recent emojis at index 0)
    val liveCategories = remember(recentEmojis.toList()) {
        ALL_EMOJI_CATEGORIES.mapIndexed { idx, cat ->
            if (idx == 0) cat.copy(emojis = recentEmojis.toList()) else cat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(theme.backgroundColor)
    ) {
        // ── Category Tabs ──────────────────────────────────────────────────
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.suggestionBgColor),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(liveCategories) { index, category ->
                val isSelected = selectedCategory == index
                // Hide "Recent" tab if no recent emojis
                if (index == 0 && recentEmojis.isEmpty()) return@itemsIndexed

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) theme.accentColor.copy(alpha = 0.3f)
                            else theme.backgroundColor
                        )
                        .clickable { selectedCategory = index }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        modifier = Modifier.size(20.dp),
                        tint = if (isSelected) theme.accentColor else theme.keyTextColor
                    )
                }
            }
        }

        // ── Category Label ─────────────────────────────────────────────────
        val currentCat = liveCategories.getOrNull(selectedCategory)
        if (currentCat != null) {
            Text(
                text = currentCat.name,
                color = theme.keyTextColor.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(theme.suggestionBgColor)
                    .padding(horizontal = 10.dp, vertical = 1.dp)
            )
        }

        // ── Emoji Grid ─────────────────────────────────────────────────────
        val displayEmojis = liveCategories.getOrNull(selectedCategory)?.emojis ?: emptyList()

        if (displayEmojis.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent emoji yet\nStart using emojis!",
                    color = theme.keyTextColor.copy(alpha = 0.4f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(displayEmojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.keyBackgroundColor)
                            .clickable(role = Role.Button, onClick = {
                                onEmojiClick(emoji)
                                // Track in recent
                                recentEmojis.remove(emoji)
                                recentEmojis.add(0, emoji)
                                if (recentEmojis.size > 40) {
                                    recentEmojis.removeLastOrNull()
                                }
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}
