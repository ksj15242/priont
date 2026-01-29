package com.lbteam.priont.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lbteam.priont.R
import com.lbteam.priont.model.TopRankItem
import com.lbteam.priont.model.TopRankSection
import com.lbteam.priont.model.type.PeriodType
import com.lbteam.priont.model.type.RankType
import com.lbteam.priont.model.type.color
import com.lbteam.priont.model.type.title
import com.lbteam.priont.ui.components.ProductTags
import com.lbteam.priont.ui.theme.PriontTheme
import java.text.DecimalFormat

private val RANKING_TABS = listOf(PeriodType.DAY_7, PeriodType.DAY_30)

@Composable
@Preview(showBackground = true)
private fun HomeRankingSectionPreview() {
    PriontTheme {
        HomeRankingSection(
            topRankSection = TopRankSection.dummy(),
            onRankingTabSelected = {},
            selectedRankingTabIndex = 0,
            onProductClicked = { }
        )
    }
}

@Composable
fun HomeRankingSection(
    topRankSection: TopRankSection,
    onRankingTabSelected: (Int) -> Unit,
    selectedRankingTabIndex: Int,
    onProductClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        RankingTab(
            onRankingTabSelected = onRankingTabSelected,
            selectedRankingTabIndex = selectedRankingTabIndex,

            )
        TopRankPager(
            topRankSection = topRankSection,
            onProductClicked = onProductClicked,
        )
    }
}

@Composable
private fun RankingTab(
    onRankingTabSelected: (Int) -> Unit,
    selectedRankingTabIndex: Int,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedRankingTabIndex,
        containerColor = MaterialTheme.colorScheme.background,
        divider = { HorizontalDivider(color = MaterialTheme.colorScheme.background) },
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = modifier.tabIndicatorOffset(tabPositions[selectedRankingTabIndex]),
                height = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },
    ) {
        RANKING_TABS.forEachIndexed { index, tag ->
            val isSelected = selectedRankingTabIndex == index

            Tab(
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selected = isSelected,
                onClick = { onRankingTabSelected(index) },
                text = {
                    Text(
                        text = tag.label,
                        style = if (isSelected) {
                            MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun TopRankPager(
    topRankSection: TopRankSection,
    onProductClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { topRankSection.pageCount })
    val currentRankType = if (pagerState.currentPage == 0) RankType.INCREASE else RankType.DECREASE

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopRankPageHeader(
            rankType = currentRankType,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 16.dp
        ) { pageIndex ->

            val rankItems = topRankSection.pages[pageIndex]
            val rankType = if (pageIndex == 0) RankType.INCREASE else RankType.DECREASE

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                rankItems.forEach { item ->
                    TopRankCard(
                        item = item,
                        rankType = rankType,
                        onProductClicked = onProductClicked
                    )
                }
            }

        }
    }
}

@Composable
private fun TopRankPageHeader(
    rankType: RankType,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TopRankPageTitle(rankType = rankType)
        Spacer(modifier = Modifier.weight(1f))
        TopRankPageIndicator(pagerState = pagerState)
    }
}

@Composable
private fun TopRankPageTitle(
    rankType: RankType,
) {
    Text(
        text = rankType.title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.ExtraBold
        ),
    )
}

@Composable
private fun TopRankPageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val isSelected = pagerState.currentPage == iteration
            val color = if (isSelected) Color(0xFF1B4332) else Color(0xFFBDBDBD)
            val width = if (isSelected) 12.dp else 8.dp

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(width = width, 8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

val priceFormat = DecimalFormat("#,###,###")

@Composable
private fun TopRankCard(
    item: TopRankItem,
    rankType: RankType,
    onProductClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onProductClicked(item.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = CardDefaults.shape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.common_product_title, item.name, item.unit),
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
            )
            ProductTags(listOf(item.variety))
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = priceFormat.format(item.price),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.common_product_price_change_rate, item.rate),
                    style = MaterialTheme.typography.labelLarge,
                    color = rankType.color
                )
            }
        }
    }
}
