package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

private enum class EmojiPanelTab(val icon: ImageVector, val label: String) {
    EMOJI(Icons.Default.Star, "Emoji"),
    GIF(Icons.Default.Gif, "GIF"),
    STICKER(Icons.Default.Category, "Sticker")
}

private val EMOJI_KEYWORDS: Map<String, List<String>> = mapOf(
    "😀" to listOf("smile", "happy", "grin"),
    "😃" to listOf("smile", "happy", "grin", "eyes"),
    "😄" to listOf("smile", "happy", "laugh"),
    "😁" to listOf("grin", "teeth"),
    "😆" to listOf("laugh", "happy"),
    "😅" to listOf("sweat", "smile", "nervous"),
    "😂" to listOf("cry", "laugh", "tears", "joy"),
    "🤣" to listOf("rofl", "laugh", "roll"),
    "😊" to listOf("blush", "shy", "happy"),
    "😇" to listOf("angel", "halo", "innocent"),
    "🙂" to listOf("slight", "smile"),
    "🙃" to listOf("upside", "silly"),
    "😉" to listOf("wink"),
    "😍" to listOf("heart", "eyes", "love", "crush"),
    "🥰" to listOf("love", "hearts", "adore"),
    "😘" to listOf("kiss", "love", "heart"),
    "😎" to listOf("cool", "sunglasses", "sun"),
    "🤩" to listOf("star", "eyes", "wow", "excited"),
    "🥳" to listOf("party", "celebrate", "birthday"),
    "😢" to listOf("cry", "sad", "tear"),
    "😭" to listOf("cry", "sob", "sad", "tears"),
    "😤" to listOf("angry", "huff", "frustrated"),
    "😠" to listOf("angry", "mad"),
    "😡" to listOf("angry", "red", "furious"),
    "🥺" to listOf("plead", "puppy", "eyes", "sad"),
    "😱" to listOf("scream", "scared", "fear", "shock"),
    "🤗" to listOf("hug", "hugger"),
    "🤔" to listOf("think", "hmm", "ponder"),
    "😐" to listOf("neutral", "meh"),
    "😑" to listOf("blank", "expressionless"),
    "🙄" to listOf("eyeroll", "annoyed"),
    "😬" to listOf("grimace", "awkward"),
    "😮" to listOf("wow", "surprise", "open", "mouth"),
    "😴" to listOf("sleep", "zzz", "tired"),
    "🤤" to listOf("drool", "yum"),
    "💀" to listOf("skull", "dead", "death"),
    "💩" to listOf("poop", "poo", "shit"),
    "👻" to listOf("ghost", "halloween", "boo"),
    "👽" to listOf("alien", "ufo", "space"),
    "🤖" to listOf("robot", "bot", "ai"),
    "❤️" to listOf("heart", "love", "red", "valentine"),
    "🧡" to listOf("heart", "orange", "love"),
    "💛" to listOf("heart", "yellow", "love"),
    "💚" to listOf("heart", "green", "love"),
    "💙" to listOf("heart", "blue", "love"),
    "💜" to listOf("heart", "purple", "love"),
    "🖤" to listOf("heart", "black", "love"),
    "💔" to listOf("broken", "heart", "sad", "break"),
    "💕" to listOf("hearts", "love", "two"),
    "💞" to listOf("revolving", "hearts", "love"),
    "💓" to listOf("heartbeat", "pulse", "love"),
    "💗" to listOf("growing", "heart", "love"),
    "💖" to listOf("sparkling", "heart", "love"),
    "💘" to listOf("cupid", "arrow", "love", "valentine"),
    "💝" to listOf("gift", "heart", "ribbon", "love"),
    "👍" to listOf("thumbs", "up", "like", "yes", "good", "ok"),
    "👎" to listOf("thumbs", "down", "dislike", "no", "bad"),
    "👋" to listOf("wave", "hello", "bye", "hi"),
    "🤝" to listOf("handshake", "deal", "agree", "shake"),
    "🙏" to listOf("pray", "please", "thanks", "namaste"),
    "✌️" to listOf("peace", "victory", "v"),
    "🤞" to listOf("fingers", "crossed", "luck", "hope"),
    "🤟" to listOf("love", "you", "rock", "sign"),
    "🤘" to listOf("rock", "horns", "metal"),
    "👌" to listOf("ok", "perfect", "good", "nice"),
    "🫶" to listOf("heart", "hands", "love"),
    "👊" to listOf("fist", "punch", "bump"),
    "✊" to listOf("fist", "punch", "solidarity"),
    "👏" to listOf("clap", "applause", "bravo", "congrats"),
    "🙌" to listOf("raise", "hands", "hooray", "yay"),
    "💪" to listOf("muscle", "strong", "flex", "bicep"),
    "🔥" to listOf("fire", "flame", "hot", "lit", "burn"),
    "⭐" to listOf("star", "favorite", "rate"),
    "🌟" to listOf("star", "glow", "sparkle", "bright"),
    "✨" to listOf("sparkle", "magic", "glitter", "shine"),
    "💯" to listOf("hundred", "perfect", "score", "done"),
    "🎉" to listOf("party", "celebrate", "tada", "confetti"),
    "🎊" to listOf("confetti", "ball", "party", "celebrate"),
    "🎈" to listOf("balloon", "party", "birthday"),
    "🎂" to listOf("cake", "birthday", "bday"),
    "🎁" to listOf("gift", "present", "box", "birthday"),
    "🎄" to listOf("christmas", "tree", "xmas"),
    "🎃" to listOf("halloween", "pumpkin", "jack"),
    "🌸" to listOf("cherry", "blossom", "flower", "spring"),
    "🌺" to listOf("flower", "hibiscus", "tropical"),
    "🌻" to listOf("sunflower", "flower", "sun"),
    "🌹" to listOf("rose", "flower", "love", "red"),
    "🌷" to listOf("tulip", "flower"),
    "🌼" to listOf("flower", "blossom"),
    "🍀" to listOf("clover", "shamrock", "luck", "irish"),
    "🌿" to listOf("herb", "plant", "green", "leaf"),
    "🍃" to listOf("leaf", "wind", "nature", "blow"),
    "🌙" to listOf("moon", "crescent", "night", "sleep"),
    "☀️" to listOf("sun", "sunny", "hot", "weather", "bright"),
    "🌈" to listOf("rainbow", "color", "pride"),
    "❄️" to listOf("snow", "cold", "winter", "freeze", "ice"),
    "⚡" to listOf("lightning", "thunder", "bolt", "electric", "flash"),
    "💧" to listOf("water", "drop", "sweat", "rain"),
    "🌊" to listOf("wave", "ocean", "sea", "water", "surf"),
    "🐶" to listOf("dog", "puppy", "pet", "bark"),
    "🐱" to listOf("cat", "kitten", "pet", "meow"),
    "🐭" to listOf("mouse", "rodent"),
    "🐹" to listOf("hamster", "pet"),
    "🐰" to listOf("rabbit", "bunny", "pet"),
    "🦊" to listOf("fox", "clever"),
    "🐻" to listOf("bear", "teddy"),
    "🐼" to listOf("panda", "bear"),
    "🐨" to listOf("koala", "australia"),
    "🐯" to listOf("tiger", "cat"),
    "🦁" to listOf("lion", "king", "mane"),
    "🐮" to listOf("cow", "moo", "milk"),
    "🐷" to listOf("pig", "oink", "ham"),
    "🐸" to listOf("frog", "toad", "ribbit"),
    "🐵" to listOf("monkey", "ape", "face"),
    "🙈" to listOf("monkey", "see", "no", "evil"),
    "🙉" to listOf("monkey", "hear", "no", "evil"),
    "🙊" to listOf("monkey", "speak", "no", "evil"),
    "🐔" to listOf("chicken", "hen", "poultry"),
    "🐧" to listOf("penguin", "bird"),
    "🐦" to listOf("bird", "tweet"),
    "🐤" to listOf("chick", "baby", "bird"),
    "🦆" to listOf("duck", "bird"),
    "🦅" to listOf("eagle", "bird", "america"),
    "🦉" to listOf("owl", "bird", "wise", "night"),
    "🐺" to listOf("wolf", "howl"),
    "🐴" to listOf("horse", "neigh"),
    "🦄" to listOf("unicorn", "magic", "fantasy"),
    "🐝" to listOf("bee", "honey", "buzz", "insect"),
    "🐛" to listOf("bug", "caterpillar", "insect"),
    "🦋" to listOf("butterfly", "insect", "pretty"),
    "🐌" to listOf("snail", "slow"),
    "🐞" to listOf("ladybug", "beetle", "insect"),
    "🐢" to listOf("turtle", "tortoise", "slow"),
    "🐍" to listOf("snake", "serpent"),
    "🐙" to listOf("octopus", "sea"),
    "🦑" to listOf("squid", "sea"),
    "🦐" to listOf("shrimp", "prawn", "sea"),
    "🦀" to listOf("crab", "sea"),
    "🐡" to listOf("blowfish", "fish"),
    "🐠" to listOf("tropical", "fish"),
    "🐟" to listOf("fish"),
    "🐬" to listOf("dolphin", "sea"),
    "🐳" to listOf("whale", "spout", "sea"),
    "🐋" to listOf("whale", "sea"),
    "🦈" to listOf("shark", "sea"),
    "🐊" to listOf("crocodile", "alligator"),
    "🐅" to listOf("tiger", "cat"),
    "🐆" to listOf("leopard", "cat"),
    "🐘" to listOf("elephant", "trunk"),
    "🦛" to listOf("hippo", "hippopotamus"),
    "🐪" to listOf("camel", "desert"),
    "🐫" to listOf("camel", "two", "hump", "desert"),
    "🦒" to listOf("giraffe", "tall", "neck"),
    "🦘" to listOf("kangaroo", "australia", "jump"),
    "🐎" to listOf("horse", "race"),
    "🐖" to listOf("pig", "oink"),
    "🐏" to listOf("ram", "sheep"),
    "🐑" to listOf("sheep", "lamb", "wool"),
    "🐐" to listOf("goat"),
    "🦌" to listOf("deer", "elk"),
    "🐕" to listOf("dog", "pet"),
    "🐈" to listOf("cat", "pet"),
    "🍏" to listOf("apple", "green", "fruit"),
    "🍎" to listOf("apple", "red", "fruit"),
    "🍐" to listOf("pear", "fruit"),
    "🍊" to listOf("orange", "fruit", "tangerine"),
    "🍋" to listOf("lemon", "fruit", "sour"),
    "🍌" to listOf("banana", "fruit"),
    "🍉" to listOf("watermelon", "fruit"),
    "🍇" to listOf("grapes", "fruit"),
    "🍓" to listOf("strawberry", "fruit"),
    "🫐" to listOf("blueberry", "fruit"),
    "🍒" to listOf("cherry", "fruit"),
    "🍑" to listOf("peach", "fruit"),
    "🥭" to listOf("mango", "fruit"),
    "🍍" to listOf("pineapple", "fruit"),
    "🥥" to listOf("coconut", "fruit"),
    "🥝" to listOf("kiwi", "fruit"),
    "🍅" to listOf("tomato", "fruit", "red"),
    "🍆" to listOf("eggplant", "aubergine"),
    "🥑" to listOf("avocado", "fruit"),
    "🥦" to listOf("broccoli", "vegetable"),
    "🥕" to listOf("carrot", "vegetable"),
    "🌽" to listOf("corn", "maize"),
    "🌶️" to listOf("pepper", "chili", "hot", "spicy"),
    "🥔" to listOf("potato"),
    "🍠" to listOf("sweet", "potato"),
    "🥐" to listOf("croissant", "bread", "french"),
    "🍞" to listOf("bread", "toast"),
    "🥖" to listOf("baguette", "bread", "french"),
    "🧀" to listOf("cheese"),
    "🥚" to listOf("egg"),
    "🍳" to listOf("egg", "fried", "breakfast", "cooking"),
    "🥞" to listOf("pancake", "breakfast"),
    "🧇" to listOf("waffle", "breakfast"),
    "🥓" to listOf("bacon", "meat"),
    "🥩" to listOf("steak", "meat"),
    "🍗" to listOf("chicken", "drumstick", "meat"),
    "🍖" to listOf("meat", "bone"),
    "🌭" to listOf("hotdog", "hot", "dog", "sausage"),
    "🍔" to listOf("burger", "hamburger"),
    "🍟" to listOf("french", "fries", "chips"),
    "🍕" to listOf("pizza"),
    "🥪" to listOf("sandwich"),
    "🌮" to listOf("taco", "mexican"),
    "🌯" to listOf("burrito", "mexican"),
    "🥗" to listOf("salad"),
    "🍝" to listOf("spaghetti", "pasta"),
    "🍜" to listOf("ramen", "noodle", "soup"),
    "🍛" to listOf("curry"),
    "🍣" to listOf("sushi", "japanese", "fish"),
    "🍤" to listOf("shrimp", "tempura"),
    "🍥" to listOf("fish", "cake"),
    "🍦" to listOf("ice", "cream", "soft"),
    "🍧" to listOf("shaved", "ice"),
    "🍨" to listOf("ice", "cream"),
    "🍩" to listOf("donut", "doughnut"),
    "🍪" to listOf("cookie"),
    "🍰" to listOf("cake", "slice"),
    "🧁" to listOf("cupcake"),
    "🍫" to listOf("chocolate"),
    "🍬" to listOf("candy", "sweet"),
    "🍭" to listOf("lollipop", "candy"),
    "☕" to listOf("coffee", "cup", "tea", "drink", "mug"),
    "🍵" to listOf("tea", "green", "drink"),
    "🧃" to listOf("juice", "box", "drink"),
    "🥤" to listOf("cup", "straw", "drink", "soda"),
    "🍺" to listOf("beer", "drink", "alcohol"),
    "🍻" to listOf("beers", "cheers", "drink"),
    "🥂" to listOf("champagne", "cheers", "celebrate", "drink"),
    "🍷" to listOf("wine", "drink", "alcohol"),
    "🍸" to listOf("cocktail", "drink", "martini"),
    "🍹" to listOf("tropical", "drink", "cocktail"),
    "🍾" to listOf("champagne", "bottle", "celebrate"),
    "⚽" to listOf("soccer", "football", "sport"),
    "🏀" to listOf("basketball", "sport"),
    "🏈" to listOf("football", "american", "sport"),
    "⚾" to listOf("baseball", "sport"),
    "🎾" to listOf("tennis", "sport"),
    "🏐" to listOf("volleyball", "sport"),
    "🏉" to listOf("rugby", "sport"),
    "🎱" to listOf("billiards", "pool", "8ball"),
    "🏓" to listOf("ping", "pong", "table", "tennis"),
    "🏸" to listOf("badminton", "sport"),
    "🥅" to listOf("goal", "net", "sport"),
    "⛳" to listOf("golf", "flag", "sport"),
    "🏆" to listOf("trophy", "winner", "award", "champion"),
    "🥇" to listOf("gold", "medal", "first", "winner"),
    "🥈" to listOf("silver", "medal", "second"),
    "🥉" to listOf("bronze", "medal", "third"),
    "🎯" to listOf("dart", "bullseye", "target", "direct"),
    "🎮" to listOf("video", "game", "controller", "play"),
    "🎲" to listOf("dice", "game", "random"),
    "🧩" to listOf("puzzle", "jigsaw", "piece"),
    "🎭" to listOf("theater", "drama", "masks", "art"),
    "🎨" to listOf("art", "palette", "paint", "color"),
    "🎬" to listOf("movie", "film", "clapper", "cinema"),
    "🎤" to listOf("microphone", "mic", "karaoke", "sing"),
    "🎧" to listOf("headphones", "music", "listen"),
    "🎼" to listOf("music", "score", "sheet"),
    "🎹" to listOf("piano", "keyboard", "music"),
    "🥁" to listOf("drum", "music"),
    "🎷" to listOf("saxophone", "music", "jazz"),
    "🎺" to listOf("trumpet", "music", "brass"),
    "🎸" to listOf("guitar", "music", "rock"),
    "🎻" to listOf("violin", "music", "strings"),
    "🚗" to listOf("car", "automobile", "red"),
    "🚕" to listOf("taxi", "cab", "car"),
    "🚙" to listOf("car", "suv", "blue"),
    "🚌" to listOf("bus", "public"),
    "🏎️" to listOf("race", "car", "fast", "formula"),
    "🚓" to listOf("police", "car", "cop"),
    "🚑" to listOf("ambulance", "emergency"),
    "🚒" to listOf("fire", "truck", "emergency"),
    "🚐" to listOf("van", "minibus"),
    "🚚" to listOf("truck", "delivery"),
    "🚛" to listOf("truck", "lorry"),
    "🚜" to listOf("tractor", "farm"),
    "🏍️" to listOf("motorcycle", "bike", "motorbike"),
    "🚲" to listOf("bicycle", "bike", "cycle"),
    "🛴" to listOf("scooter"),
    "🛵" to listOf("motor", "scooter"),
    "✈️" to listOf("airplane", "plane", "fly", "flight"),
    "🚀" to listOf("rocket", "space", "launch", "fast"),
    "🛸" to listOf("ufo", "flying", "saucer", "alien"),
    "🚢" to listOf("ship", "boat", "cruise"),
    "⚓" to listOf("anchor", "ship", "sea"),
    "🌍" to listOf("earth", "globe", "world", "europe", "africa"),
    "🌎" to listOf("earth", "globe", "world", "americas"),
    "🌏" to listOf("earth", "globe", "world", "asia"),
    "🗺️" to listOf("map", "world"),
    "🧭" to listOf("compass", "navigation"),
    "🏔️" to listOf("mountain", "snow"),
    "⛰️" to listOf("mountain"),
    "🏖️" to listOf("beach", "umbrella", "sand"),
    "🏜️" to listOf("desert", "sand"),
    "🌋" to listOf("volcano", "eruption"),
    "🏕️" to listOf("camping", "tent"),
    "🏠" to listOf("house", "home", "building"),
    "🏡" to listOf("house", "garden", "home"),
    "🏢" to listOf("office", "building", "corporate"),
    "🏣" to listOf("post", "office"),
    "🏥" to listOf("hospital", "medical"),
    "🏦" to listOf("bank", "money"),
    "🏨" to listOf("hotel"),
    "🏩" to listOf("love", "hotel"),
    "🏪" to listOf("convenience", "store", "shop"),
    "🏫" to listOf("school", "education"),
    "🏬" to listOf("department", "store"),
    "🏭" to listOf("factory", "industrial"),
    "🏯" to listOf("castle", "japanese"),
    "🏰" to listOf("castle", "european"),
    "💒" to listOf("wedding", "church", "love"),
    "🗼" to listOf("tower", "tokyo"),
    "🗽" to listOf("liberty", "statue", "freedom", "america"),
    "⛪" to listOf("church", "cross", "religion"),
    "🕌" to listOf("mosque", "islam"),
    "⛩️" to listOf("shinto", "shrine", "japanese"),
    "⛲" to listOf("fountain"),
    "⛺" to listOf("tent", "camping"),
    "🌅" to listOf("sunrise", "sun", "morning"),
    "🌄" to listOf("sunrise", "mountains"),
    "🌠" to listOf("shooting", "star"),
    "🎇" to listOf("sparkler", "fireworks"),
    "🎆" to listOf("fireworks", "celebrate"),
    "🌇" to listOf("sunset", "city", "evening"),
    "🌆" to listOf("cityscape", "dusk"),
    "🏙️" to listOf("city", "skyline"),
    "🌃" to listOf("night", "stars", "city"),
    " watches" to listOf("watch", "time"),
    "📱" to listOf("phone", "mobile", "cell", "telephone"),
    "💻" to listOf("laptop", "computer", "mac"),
    "⌨️" to listOf("keyboard", "type"),
    "🖥️" to listOf("desktop", "computer", "monitor"),
    "🖨️" to listOf("printer"),
    "🖱️" to listOf("mouse", "computer"),
    "💾" to listOf("floppy", "disk", "save"),
    "💿" to listOf("cd", "disk", "optical"),
    "📀" to listOf("dvd", "disk"),
    "📷" to listOf("camera", "photo"),
    "📸" to listOf("camera", "flash", "photo"),
    "📹" to listOf("video", "camera", "record"),
    "🎥" to listOf("movie", "camera", "film"),
    "📞" to listOf("phone", "telephone", "call"),
    "☎️" to listOf("phone", "telephone", "call"),
    "📟" to listOf("pager"),
    "📠" to listOf("fax"),
    "📺" to listOf("tv", "television"),
    "📻" to listOf("radio"),
    "⏱️" to listOf("stopwatch", "timer"),
    "⏲️" to listOf("timer", "clock"),
    "⏰" to listOf("alarm", "clock", "time", "morning"),
    "🕰️" to listOf("clock", "mantle"),
    "⌛" to listOf("hourglass", "time"),
    "⏳" to listOf("hourglass", "time", "flowing"),
    "📡" to listOf("satellite", "antenna"),
    "🔋" to listOf("battery", "power", "charge"),
    "🔌" to listOf("plug", "electric", "power"),
    "💡" to listOf("light", "bulb", "idea", "bright"),
    "🔦" to listOf("flashlight", "torch", "light"),
    "🕯️" to listOf("candle", "light"),
    "💰" to listOf("money", "bag", "dollar", "rich"),
    "💳" to listOf("credit", "card", "money"),
    "💎" to listOf("diamond", "gem", "jewel", "precious"),
    "⚖️" to listOf("balance", "scale", "justice", "law"),
    "🔧" to listOf("wrench", "tool", "fix"),
    "🔨" to listOf("hammer", "tool", "build"),
    "🔧" to listOf("tool", "fix", "repair"),
    "🔑" to listOf("key", "lock", "password"),
    "🗝️" to listOf("key", "old"),
    "🔒" to listOf("lock", "closed", "secure", "private"),
    "🔓" to listOf("lock", "open", "unlocked"),
    "📦" to listOf("package", "box", "delivery", "parcel"),
    "📫" to listOf("mailbox", "closed", "mail"),
    "📬" to listOf("mailbox", "open", "mail"),
    "📮" to listOf("postbox", "mail", "letter"),
    "✏️" to listOf("pencil", "edit", "write"),
    "✒️" to listOf("pen", "write"),
    "📝" to listOf("memo", "note", "write", "document"),
    "📁" to listOf("folder", "file"),
    "📂" to listOf("folder", "open", "file"),
    "📅" to listOf("calendar", "date"),
    "📆" to listOf("calendar", "date"),
    "📈" to listOf("chart", "up", "trend", "growth"),
    "📉" to listOf("chart", "down", "trend", "decline"),
    "📊" to listOf("chart", "bar", "graph", "statistics"),
    "📋" to listOf("clipboard", "list"),
    "📌" to listOf("pin", "pushpin", "thumbtack"),
    "📍" to listOf("pin", "round", "location"),
    "📎" to listOf("paperclip", "attach"),
    "✂️" to listOf("scissors", "cut"),
    "🔍" to listOf("search", "magnifying", "glass", "find", "zoom"),
    "🔎" to listOf("search", "magnifying", "glass", "find"),
    "📖" to listOf("book", "open", "read"),
    "📚" to listOf("books", "read", "library"),
    "📰" to listOf("newspaper", "news", "paper"),
    "🔖" to listOf("bookmark", "mark", "tag"),
    "🏷️" to listOf("label", "tag"),
    "✉️" to listOf("envelope", "mail", "email", "letter"),
    "📧" to listOf("email", "mail", "envelope"),
    "📨" to listOf("email", "incoming", "receive"),
    "📩" to listOf("email", "arrow", "send"),
    "📤" to listOf("outbox", "send", "arrow"),
    "📥" to listOf("inbox", "receive", "arrow"),
    "📦" to listOf("package", "box", "delivery"),
    "✅" to listOf("check", "done", "complete", "ok", "yes"),
    "❌" to listOf("cross", "no", "wrong", "delete"),
    "❗" to listOf("exclamation", "important", "alert"),
    "❓" to listOf("question", "ask", "why", "help"),
    "‼️" to listOf("double", "exclamation", "bang"),
    "⁉️" to listOf("interrobang", "question", "exclamation"),
    "🔴" to listOf("red", "circle", "dot"),
    "🟠" to listOf("orange", "circle"),
    "🟡" to listOf("yellow", "circle"),
    "🟢" to listOf("green", "circle"),
    "🔵" to listOf("blue", "circle"),
    "🟣" to listOf("purple", "circle"),
    "⚫" to listOf("black", "circle", "dot"),
    "⚪" to listOf("white", "circle", "dot"),
    "🔺" to listOf("triangle", "red", "up"),
    "🔻" to listOf("triangle", "red", "down"),
    "▶️" to listOf("play", "button", "right"),
    "⏸️" to listOf("pause", "button"),
    "⏹️" to listOf("stop", "button"),
    "⏺️" to listOf("record", "button"),
    "⏭️" to listOf("next", "track", "forward"),
    "⏮️" to listOf("previous", "track", "backward"),
    "🔀" to listOf("shuffle", "random", "crossed"),
    "🔁" to listOf("repeat", "loop"),
    "🔂" to listOf("repeat", "one", "loop"),
    "🔊" to listOf("volume", "loud", "sound", "speaker"),
    "🔉" to listOf("volume", "medium", "sound"),
    "🔈" to listOf("volume", "low", "sound", "muted"),
    "🔇" to listOf("mute", "silent", "quiet", "off"),
    "📣" to listOf("megaphone", "loud", "announce"),
    "📢" to listOf("loudspeaker", "announcement"),
    "🔔" to listOf("bell", "notification", "alert"),
    "🔕" to listOf("bell", "mute", "silent", "off"),
    "🎵" to listOf("music", "note"),
    "🎶" to listOf("music", "notes", "melody"),
    "🏧" to listOf("atm", "money", "cash"),
    "♻️" to listOf("recycle", "green", "environment"),
    "⚛️" to listOf("atom", "science", "physics"),
    "🔰" to listOf("beginner", "japanese", "level"),
    "🔱" to listOf("trident", "emblem"),
    "📛" to listOf("name", "badge"),
    "®️" to listOf("registered", "trademark"),
    "©️" to listOf("copyright"),
    "™️" to listOf("trademark", "tm"),
    " #️⃣" to listOf("hash", "number", "pound", "hashtag"),
    "0️⃣" to listOf("zero", "0"),
    "1️⃣" to listOf("one", "1"),
    "2️⃣" to listOf("two", "2"),
    "3️⃣" to listOf("three", "3"),
    "4️⃣" to listOf("four", "4"),
    "5️⃣" to listOf("five", "5"),
    "6️⃣" to listOf("six", "6"),
    "7️⃣" to listOf("seven", "7"),
    "8️⃣" to listOf("eight", "8"),
    "9️⃣" to listOf("nine", "9"),
    "🔟" to listOf("ten", "10"),
    "🔢" to listOf("numbers", "input"),
    "🔣" to listOf("symbols", "input"),
    "🔤" to listOf("abc", "input", "latin"),
    "🔡" to listOf("ab", "input", "latin"),
    "🔠" to listOf("A", "input", "latin"),
    "ℹ️" to listOf("info", "information"),
    "🆗" to listOf("ok", "button"),
    "🆒" to listOf("cool", "button"),
    "🆕" to listOf("new", "button"),
    "🆓" to listOf("free", "button"),
    "🆙" to listOf("up", "button"),
    "🆚" to listOf("vs", "button"),
    "🈁" to listOf("japanese", "here", "button"),
    "🈂️" to listOf("japanese", "service", "charge", "button"),
    "🈷️" to listOf("japanese", "monthly", "amount", "button"),
    "🈶" to listOf("japanese", "not", "free", "charge", "button"),
    "🈯" to listOf("japanese", "reserved", "button"),
    "🉐" to listOf("japanese", "bargain", "button"),
    "🈹" to listOf("japanese", "discount", "button"),
    "🈚" to listOf("japanese", "free", "charge", "button"),
    "🈲" to listOf("japanese", "prohibited", "button"),
    "🉑" to listOf("japanese", "acceptable", "button"),
    "🈸" to listOf("japanese", "application", "button"),
    "🈴" to listOf("japanese", "passing", "grade", "button"),
    "🈳" to listOf("japanese", "vacancy", "button"),
    "㊗️" to listOf("japanese", "congratulations", "button"),
    "㊙️" to listOf("japanese", "secret", "button"),
    "㊗" to listOf("japanese", "congratulations"),
    "㊙" to listOf("japanese", "secret"),
    "♈" to listOf("aries", "zodiac"),
    "♉" to listOf("taurus", "zodiac"),
    "♊" to listOf("gemini", "zodiac"),
    "♋" to listOf("cancer", "zodiac"),
    "♌" to listOf("leo", "zodiac"),
    "♍" to listOf("virgo", "zodiac"),
    "♎" to listOf("libra", "zodiac"),
    "♏" to listOf("scorpio", "zodiac"),
    "♐" to listOf("sagittarius", "zodiac"),
    "♑" to listOf("capricorn", "zodiac"),
    "♒" to listOf("aquarius", "zodiac"),
    "♓" to listOf("pisces", "zodiac"),
    "⛎" to listOf("ophiuchus", "zodiac"),
    "🆎" to listOf("ab", "blood", "button"),
    "🆑" to listOf("cl", "button"),
    "🆘" to listOf("sos", "button"),
    "🅾️" to listOf("o", "blood", "button"),
    "🚾" to listOf("wc", "toilet", "restroom"),
    "🚹" to listOf("mens", "toilet", "man"),
    "🚺" to listOf("womens", "toilet", "woman"),
    "🚼" to listOf("baby", "symbol"),
    "🚻" to listOf("restroom", "toilet"),
    "🚽" to listOf("toilet"),
    "🚿" to listOf("shower"),
    "🛁" to listOf("bath", "bathtub"),
    "⚠️" to listOf("warning", "caution"),
    "🚫" to listOf("forbidden", "prohibited", "no"),
    "⛔" to listOf("entry", "forbidden", "no"),
    "🚳" to listOf("no", "bicycles"),
    "🚭" to listOf("no", "smoking"),
    "🚯" to listOf("no", "littering"),
    "🚱" to listOf("no", "drinking", "water"),
    "🚷" to listOf("no", "pedestrians"),
    "📵" to listOf("no", "phones"),
    "🔞" to listOf("underage", "18"),
    "☢️" to listOf("radioactive", "nuclear"),
    "☣️" to listOf("biohazard"),
    "⬆️" to listOf("arrow", "up", "direction"),
    "↗️" to listOf("arrow", "up", "right"),
    "➡️" to listOf("arrow", "right", "direction"),
    "↘️" to listOf("arrow", "down", "right"),
    "⬇️" to listOf("arrow", "down", "direction"),
    "↙️" to listOf("arrow", "down", "left"),
    "⬅️" to listOf("arrow", "left", "direction"),
    "↖️" to listOf("arrow", "up", "left"),
    "↕️" to listOf("arrow", "up", "down"),
    "↔️" to listOf("arrow", "left", "right"),
    "↩️" to listOf("arrow", "return", "enter"),
    "↪️" to listOf("arrow", "enter"),
    "⤴️" to listOf("arrow", "right", "turn"),
    "⤵️" to listOf("arrow", "right", "turn"),
    "🔄" to listOf("arrows", "cycle", "refresh"),
    "🔙" to listOf("back", "arrow"),
    "🔚" to listOf("end", "arrow"),
    "🔛" to listOf("on", "arrow"),
    "🔜" to listOf("soon", "arrow"),
    "🔝" to listOf("top", "arrow"),
    "▶️" to listOf("play"),
    "🔀" to listOf("shuffle"),
    "▶" to listOf("play", "button"),
    "🏳️" to listOf("flag", "white"),
    "🏴" to listOf("flag", "black"),
    "🏁" to listOf("flag", "checkered", "racing"),
    "🚩" to listOf("flag", "red", "triangular"),
    "🎌" to listOf("flags", "crossed", "japanese"),
    "🏴‍☠️" to listOf("pirate", "flag"),
    "🏳️‍🌈" to listOf("rainbow", "flag", "pride", "gay"),
    "🇺🇸" to listOf("united", "states", "america", "usa", "flag"),
    "🇬🇧" to listOf("united", "kingdom", "britain", "uk", "flag"),
    "🇮🇳" to listOf("india", "flag"),
    "🇧🇩" to listOf("bangladesh", "flag"),
    "🇸🇦" to listOf("saudi", "arabia", "flag"),
    "🇦🇪" to listOf("united", "arab", "emirates", "uae", "flag"),
    "🇹🇷" to listOf("turkey", "flag"),
    "🇩🇪" to listOf("germany", "flag"),
    "🇫🇷" to listOf("france", "flag"),
    "🇮🇹" to listOf("italy", "flag"),
    "🇪🇸" to listOf("spain", "flag"),
    "🇧🇷" to listOf("brazil", "flag"),
    "🇯🇵" to listOf("japan", "flag"),
    "🇰🇷" to listOf("korea", "south", "flag"),
    "🇨🇳" to listOf("china", "flag"),
    "🇷🇺" to listOf("russia", "flag"),
    "🇨🇦" to listOf("canada", "flag"),
    "🇦🇺" to listOf("australia", "flag"),
    "🏳️" to listOf("white", "flag"),
    "🏴" to listOf("black", "flag"),
    "🇨🇭" to listOf("switzerland", "flag"),
    "🇸🇪" to listOf("sweden", "flag"),
    "🇳🇱" to listOf("netherlands", "flag"),
    "🇧🇪" to listOf("belgium", "flag"),
    "🇦🇷" to listOf("argentina", "flag"),
    "🇲🇽" to listOf("mexico", "flag"),
    "🇳🇬" to listOf("nigeria", "flag"),
    "🇿🇦" to listOf("south", "africa", "flag"),
    "🇪🇬" to listOf("egypt", "flag"),
    "🇰🇪" to listOf("kenya", "flag"),
    "🇵🇰" to listOf("pakistan", "flag"),
    "🇮🇩" to listOf("indonesia", "flag"),
    "🇹🇭" to listOf("thailand", "flag"),
    "🇻🇳" to listOf("vietnam", "flag"),
    "🇵🇭" to listOf("philippines", "flag"),
    "🇲🇾" to listOf("malaysia", "flag"),
    "🇸🇬" to listOf("singapore", "flag"),
    "🇳🇿" to listOf("new", "zealand", "flag"),
    "🇮🇪" to listOf("ireland", "flag"),
    "🇮🇱" to listOf("israel", "flag"),
    "🇺🇦" to listOf("ukraine", "flag"),
    "🇵🇱" to listOf("poland", "flag"),
    "🇳🇴" to listOf("norway", "flag"),
    "🇫🇮" to listOf("finland", "flag"),
    "🇩🇰" to listOf("denmark", "flag"),
    "🇬🇷" to listOf("greece", "flag"),
    "🇹🇭" to listOf("thailand", "flag"),
    "🇨🇴" to listOf("colombia", "flag"),
    "🇨🇱" to listOf("chile", "flag"),
    "🇵🇪" to listOf("peru", "flag"),
    "🇪🇨" to listOf("ecuador", "flag"),
    "🇻🇪" to listOf("venezuela", "flag"),
    "🇭🇰" to listOf("hong", "kong", "flag"),
    "🇹🇼" to listOf("taiwan", "flag"),
    "🇲🇲" to listOf("myanmar", "flag"),
    "🇱🇰" to listOf("sri", "lanka", "flag"),
    "🇳🇵" to listOf("nepal", "flag"),
    "🇦🇫" to listOf("afghanistan", "flag"),
    "🇮🇶" to listOf("iraq", "flag"),
    "🇮🇷" to listOf("iran", "flag"),
    "🇯🇴" to listOf("jordan", "flag"),
    "🇱🇧" to listOf("lebanon", "flag"),
    "🇶🇦" to listOf("qatar", "flag"),
    "🇰🇼" to listOf("kuwait", "flag"),
    "🇧🇭" to listOf("bahrain", "flag"),
    "🇴🇲" to listOf("oman", "flag"),
    "🇾🇪" to listOf("yemen", "flag"),
    "🇵🇸" to listOf("palestine", "flag"),
    "🇩🇿" to listOf("algeria", "flag"),
    "🇲🇦" to listOf("morocco", "flag"),
    "🇹🇳" to listOf("tunisia", "flag"),
    "🇱🇾" to listOf("libya", "flag"),
    "🇸🇩" to listOf("sudan", "flag"),
    "🇪🇹" to listOf("ethiopia", "flag"),
    "🇰🇿" to listOf("kazakhstan", "flag"),
    "🇺🇿" to listOf("uzbekistan", "flag"),
    "🇦🇿" to listOf("azerbaijan", "flag"),
    "🇬🇪" to listOf("georgia", "flag"),
    "🇦🇲" to listOf("armenia", "flag")
)

@Composable
fun EmojiPanel(theme: KeyboardTheme, onEmojiClick: (String) -> Unit, onBackspace: () -> Unit, recentEmojis: MutableList<String> = remember { mutableStateListOf() }, onRecentEmojisChanged: (List<String>) -> Unit = {}, onSearchToggle: () -> Unit = {}) {
    var selectedCategory by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val liveCategories = remember(recentEmojis.toList()) {
        ALL_EMOJI_CATEGORIES.mapIndexed { idx, cat ->
            if (idx == 0) cat.copy(emojis = recentEmojis.toList()) else cat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(theme.backgroundColor)
    ) {
        when (selectedTab) {
            0 -> {
                // ── Search bar (tap to activate search mode) ─────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(theme.suggestionBgColor)
                        .clickable { onSearchToggle() }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search emoji",
                            modifier = Modifier.size(16.dp),
                            tint = theme.keyTextColor.copy(alpha = 0.4f)
                        )
                        Text(
                            text = "Search emoji...",
                            color = theme.keyTextColor.copy(alpha = 0.4f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }

                // ── Category Tabs ────────────────────────────────────────
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(theme.suggestionBgColor),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(liveCategories) { index, category ->
                        val isSelected = selectedCategory == index
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

                    // Category label + delete
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(theme.suggestionBgColor)
                            .padding(horizontal = 10.dp, vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentCat = liveCategories.getOrNull(selectedCategory)
                        if (currentCat != null) {
                            Text(
                                text = currentCat.name,
                                color = theme.keyTextColor.copy(alpha = 0.5f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(theme.keyBackgroundColor)
                                .clickable(role = Role.Button, onClick = onBackspace),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(18.dp),
                                tint = theme.keyTextColor
                            )
                        }
                    }

                    // Emoji grid
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
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                                            recentEmojis.remove(emoji)
                                            recentEmojis.add(0, emoji)
                                            if (recentEmojis.size > 40) recentEmojis.removeLastOrNull()
                                            onRecentEmojisChanged(recentEmojis.toList())
                                        }),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                    }
                }
            }
            1 -> {
                // ── GIF Placeholder ──────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Gif,
                            contentDescription = "GIF",
                            modifier = Modifier.size(48.dp),
                            tint = theme.keyTextColor.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "GIF support coming soon",
                            color = theme.keyTextColor.copy(alpha = 0.4f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            2 -> {
                // ── Sticker Placeholder ─────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Category,
                            contentDescription = "Sticker",
                            modifier = Modifier.size(48.dp),
                            tint = theme.keyTextColor.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "Sticker support coming soon",
                            color = theme.keyTextColor.copy(alpha = 0.4f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        // ── Bottom Tabs (Emoji | GIF | Sticker) ────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.suggestionBgColor)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EmojiPanelTab.entries.forEachIndexed { index, tab ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) theme.accentColor.copy(alpha = 0.3f)
                            else androidx.compose.ui.graphics.Color.Transparent
                        )
                        .clickable { selectedTab = index }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            modifier = Modifier.size(18.dp),
                            tint = if (isSelected) theme.accentColor else theme.keyTextColor
                        )
                        Text(
                            text = tab.label,
                            color = if (isSelected) theme.accentColor else theme.keyTextColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// EmojiSearchBar — shown when emoji search is active (main keyboard stays visible)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EmojiSearchBar(
    theme: KeyboardTheme,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    recentEmojis: List<String>,
    onEmojiClick: (String) -> Unit,
    visibleRows: Int = 2,
    horizontal: Boolean = true
) {
    val isSearching = searchQuery.isNotBlank()
    val searchResults = remember(searchQuery) {
        if (!isSearching) emptyList()
        else {
            val query = searchQuery.lowercase().trim()
            val matched = mutableListOf<String>()
            for (cat in ALL_EMOJI_CATEGORIES.drop(1)) {
                for (emoji in cat.emojis) {
                    val keywords = EMOJI_KEYWORDS[emoji] ?: emptyList()
                    if (keywords.any { it.contains(query) }) {
                        if (emoji !in matched) matched.add(emoji)
                    }
                }
            }
            matched
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(theme.backgroundColor)
    ) {
        // ── Search query bar ────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(theme.suggestionBgColor)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = theme.keyTextColor.copy(alpha = 0.5f)
            )

            // Text with blinking cursor
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = searchQuery,
                        color = theme.keyTextColor,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
                // Blinking cursor
                var cursorVisible by remember { mutableStateOf(true) }
                LaunchedEffect(searchQuery) {
                    cursorVisible = true
                    while (true) {
                        delay(500)
                        cursorVisible = !cursorVisible
                    }
                }
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .height(16.dp)
                        .background(
                            if (cursorVisible) theme.accentColor
                            else Color.Transparent
                        )
                )
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Type to search...",
                        color = theme.keyTextColor.copy(alpha = 0.4f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(theme.accentColor)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close",
                    modifier = Modifier.size(12.dp),
                    tint = Color.White
                )
            }
        }

        // ── Search results ───────────────────────────────────────────────
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSearching) "No emoji found" else "Type to search emojis",
                    color = theme.keyTextColor.copy(alpha = 0.4f),
                    fontSize = 12.sp
                )
            }
        } else {
            if (horizontal) {
                // Distribute ALL results across N visible rows, each scrolls left-right
                val rowsCount = minOf(visibleRows, searchResults.size)
                val emojisPerRow = (searchResults.size + rowsCount - 1) / rowsCount
                val rows = searchResults.chunked(emojisPerRow)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rows.forEach { rowEmojis ->
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(rowEmojis) { emoji ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(theme.keyBackgroundColor)
                                        .clickable { onEmojiClick(emoji) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                // Vertical scrollable grid
                val emojisPerRow = 6
                val rows = searchResults.chunked(emojisPerRow)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rows.forEach { rowEmojis ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            rowEmojis.forEach { emoji ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(theme.keyBackgroundColor)
                                        .clickable { onEmojiClick(emoji) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                }
                            }
                            repeat(emojisPerRow - rowEmojis.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
