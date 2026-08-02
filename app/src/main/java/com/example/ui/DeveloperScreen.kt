package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.developer_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.developer_name),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.developer_bio),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = stringResource(R.string.developer_blood),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Profile Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(18.dp)
                ) {
                    Text(stringResource(R.string.developer_education), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(icon = Icons.Default.School, text = stringResource(R.string.developer_edu_bsc))
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoRow(icon = Icons.Default.School, text = stringResource(R.string.developer_edu_hsc))
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoRow(icon = Icons.Default.School, text = stringResource(R.string.developer_edu_ssc))

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(R.string.developer_skills), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(icon = Icons.Default.Code, text = stringResource(R.string.developer_skill_dart))
                    InfoRow(icon = Icons.Default.PhoneAndroid, text = stringResource(R.string.developer_skill_android))
                    InfoRow(icon = Icons.Default.DesignServices, text = stringResource(R.string.developer_skill_ui))
                    InfoRow(icon = Icons.Default.Terminal, text = stringResource(R.string.developer_skill_tools))

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(R.string.developer_contact), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(icon = Icons.Default.Email, text = stringResource(R.string.developer_email))
                    InfoRow(icon = Icons.Default.Language, text = stringResource(R.string.developer_github))
                    InfoRow(icon = Icons.Default.Public, text = stringResource(R.string.developer_web))
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.made_with), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFD93025), modifier = Modifier.size(16.dp))
                Text(text = stringResource(R.string.made_by), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
