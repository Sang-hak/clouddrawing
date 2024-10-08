package kr.ac.kookmin.clouddrawing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.woong.compose.grid.SimpleGridCells
import io.woong.compose.grid.VerticalGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.ac.kookmin.clouddrawing.components.LeftCloseBtn
import kr.ac.kookmin.clouddrawing.dto.Post
import kr.ac.kookmin.clouddrawing.dto.User
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CloudListActivity : ComponentActivity() {

    private var allPost : List<Post> = listOf()

    private val today = mutableStateOf(0)
    private val month = mutableStateOf(0)
    private val recent = mutableStateOf(0)

    companion object {
        private val TAG: String = "CloudListActiity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            allPost = Post.getPostByUID(User.getCurrentUser()?.uid).toMutableList()
            Log.e("Testing", "vandvandjv : ${allPost}")
        }

        setContent {
            val verticalScroll = rememberScrollState()

            CloudList(
                verticalScroll = verticalScroll,
                leftCloseBtn = { finish() },
                today = today, month = month, recent = recent,
                postList = allPost,
            )
        }

        CoroutineScope(Dispatchers.Main).launch {
            launch {
                today.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
                )
                Log.d(TAG, "today: ${today.value} / ${Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())}")
            }
            launch {
                month.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusMonths(1).toInstant()))
                )
                Log.d(TAG, "month: ${month.value} / ${Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusMonths(1).toInstant()))}")
            }
            launch {
                recent.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusYears(50).toInstant()))
                )
                Log.d(TAG, "recent: ${recent.value} / ${Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusYears(50).toInstant()))}")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.Main).launch {
            allPost = Post.getPostByUID(User.getCurrentUser()?.uid).toMutableList()
            Log.e("Testing", "vandvandjv : ${allPost}")
            launch {
                today.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
                )
                Log.d(TAG, "today: ${today.value} / ${Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())}")
            }
            launch {
                month.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusMonths(1).toInstant()))
                )
                Log.d(TAG, "month: ${month.value} / ${Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusMonths(1).toInstant()))}")
            }
            launch {
                recent.value = Post.getPostCountByDate(User.getCurrentUser()!!.uid!!,
                    Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusYears(50).toInstant()))
                )
                Log.d(TAG, "recent: ${recent.value} / ${Date.from(Instant.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusYears(50).toInstant()))}")
            }
        }
    }
}



@Preview
@Composable
fun CloudList(
    leftCloseBtn: () -> Unit = {},
    verticalScroll: ScrollState = rememberScrollState(),
    postList : List<Post> = listOf(),
    today: MutableState<Int> = mutableStateOf(0),
    month: MutableState<Int> = mutableStateOf(0),
    recent: MutableState<Int> = mutableStateOf(0)
) {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = Color(0xFFFFFFFF))
    ) {
        Row(
            modifier = Modifier
                .padding(start = 31.dp, end = 31.dp, top = 60.dp)
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LeftCloseBtn(leftCloseBtn)
            Text(
                text = "내가 그린 구름",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF474747)
                )
            )
            Spacer(Modifier.width(1.dp))
        } // Header done.
        Spacer(Modifier.defaultMinSize(minHeight = 20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 31.dp, end = 31.dp, bottom = 10.dp)
                .shadow(
                    elevation = 20.dp, spotColor = Color(0x0D000000),
                    ambientColor = Color(0x0D000000)
                )
                .border(
                    width = 1.dp, color = Color(0xFFF4F4F4),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .background(
                    color = Color(0xFFB4CCFF),
                    shape = RoundedCornerShape(size = 20.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // 가운데 정렬
                verticalArrangement = Arrangement.Center // 수직 중앙 정렬
            ) {
                Text(
                    text = "오늘은 구름 ${today.value} 개를 그렸어요. \n" +
                            "이번 달 구름 ${month.value} 개를 그렸어요. \n" + "" +
                            "지금까지 구름 ${recent.value} 개를 그렸어요.",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    ),
                    maxLines = 4,
                    textAlign = TextAlign.End
                )
                Spacer(Modifier.height(10.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 40.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Text(text = "구름 모아보기",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF474747),
                ))
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.defaultMinSize(minHeight = 20.dp))
        ClContentBox(postList)
    }
}
@Preview
@Composable
fun ClContentBox(
    postList : List<Post> = listOf()
) {
    var locNum by remember { mutableStateOf("전체") }

    Column(
        modifier = Modifier
            .padding(start = 31.dp, end = 31.dp, bottom = 10.dp)
            .shadow(
                elevation = 20.dp,
                spotColor = Color(0x0D000000),
                ambientColor = Color(0x0D000000)
            )
            .border(
                width = 1.dp, color = Color(0xFFF4F4F4),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .fillMaxWidth(1f)
            .height(570.dp)
            .background(color = Color(0xFFFFFFFF))
    ) {
        val locations = listOf(
            "전체", "서울", "경기", "인천", "강원", "충북",
            "충남", "대전", "세종", "경북", "경남", "대구",
            "울산", "부산", "전북", "전남", "광주", "제주"
        )
        var selectedLocation by remember { mutableStateOf("전체") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 17.dp, end = 27.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(54.dp)
                    .padding(end = 10.dp)

            ) {
                locations.forEach { location ->
                    val isSelected = selectedLocation == location // 현재 선택된 위치인지 확인

                    Text(
                        text = location,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 16.dp)
                            .clickable {
                                selectedLocation = location // 위치를 클릭하면 선택된 위치 업데이트
                                locNum = location
                            },
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = if (isSelected) Color(0xFF326AFF) else Color(0xFF848484), // 선택된 위치일 때 색상 변경
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 5.dp, bottom = 15.dp)
                    .width(1.dp),
                color = Color(0xFFC9C9C9),
                thickness = 1.dp
            )
            Crossfade(targetState = locNum, label = "") { location ->
                VerticalGrid(
                    columns = SimpleGridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, bottom=20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    run breaker@{
                        if (location == locations[0]) {
                            postList.forEach {
                                CLContentCard(it)
                            }
                            return@breaker
                        }

                        locations.forEach { it ->
                            if (location == it) {
                                postList.filter { post -> post.address?.startsWith(it) ?: false }.forEach {
                                    CLContentCard(it)
                                }
                                return@breaker
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun CLContentCard(
    post: Post? = null
) {
    val context = LocalContext.current

    val intent = Intent(context, CloudContentActivity::class.java)

    Column(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .padding(top = 10.dp)
            .shadow(
                elevation = 20.dp, spotColor = Color(0x0D000000),
                ambientColor = Color(0x0D000000)
            )
            .border(
                width = 1.dp, color = Color(0xFFF4F4F4),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .fillMaxHeight(1f)
            .clickable {
                intent.putExtra("postId", post?.id)
                context.startActivity(intent)
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 12.dp, start = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.height(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.f_cm_location),
                    contentDescription = "place icon",
                    modifier = Modifier
                        .width(13.dp)
                        .height(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = post?.address ?: "TB_Location",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF727272),
                    )
                )
            }

        }


        Spacer(Modifier.height(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = post?.title ?: "TB_Title",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF474747)
                )
            )
        }

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(end = 9.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = SimpleDateFormat("yyyy-MM-dd").format(
                    post?.postTime?.toDate() ?: Date()
                ),
                style = TextStyle(
                    fontSize = 7.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF9C9C9C),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}
