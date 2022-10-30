package com.github.cleancompose.presentation.repo.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.cleancompose.R
import com.github.cleancompose.domain.model.repo.Repo

@Composable
fun RepoListItem(repo: Repo, onRepoClick: (Repo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRepoClick(repo) },
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 8.dp
                ),
                color = MaterialTheme.colors.surface.copy(
                    alpha = 0.2f
                )
            ) {
                // SubcomposeAsyncImage is a variant of AsyncImage that uses subcomposition to
                // provide a slot API for AsyncImagePainter's states instead of using Painters.
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(repo.owner.avatarURL)
                        .crossfade(true)
                        .build(),
                    loading = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    contentDescription = stringResource(R.string.image_description),
                    modifier = Modifier.height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = repo.name,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 22.sp),
                    color = Color.Black
                )
                Row {
                    Text(
                        text = "${repo.visibility}: ",
                        style = typography.body2,
                    )
                    Text(
                        text = (!repo.isPrivate).toString(),
                        style = typography.body2,
                    )
                }
            }
        }
    }
}