package kr.ac.kookmin.clouddrawing

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kr.ac.kookmin.clouddrawing.components.LeftCloseBtn


class CloudDrawingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = mutableStateOf("")
        val date = mutableStateOf("")
        val locations = mutableStateOf("")
        val friends = mutableStateOf("")
        val mainContent = mutableStateOf("")

        setContent {
            val scrollState = rememberScrollState()

            CDBackground(
                title = title,
                date = date,
                locations = locations,
                friends = friends,
                mainContent = mainContent,
                scrollState = scrollState,
                onClickBack = { finish() },
                onClickSave = { /* TODO: Save button clicked */ }
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun CDBackground(
    title: MutableState<String> = mutableStateOf(""),
    date: MutableState<String> = mutableStateOf(""),
    locations: MutableState<String> = mutableStateOf(""),
    friends: MutableState<String> = mutableStateOf(""),
    mainContent: MutableState<String> = mutableStateOf(""),
    scrollState: ScrollState = rememberScrollState(),

    onClickBack: () -> Unit = {},
    onClickSave: (it: List<Uri>) -> Unit = {},

    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    var selectImages by remember {
        mutableStateOf(listOf<Uri>())
    }
    val getPhotoFromGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            selectImages = it
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 13.dp, top = 33.dp, end = 14.dp, bottom = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LeftCloseBtn(onClick = onClickBack)
            Text(
                text = "구름 그리기",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF454545),
                )
            )
            Text(
                text = "저장",
                style = TextStyle(
                    fontSize = 17.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF6891FF),
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(3.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(size = 25.dp)
                )
        )

        TextField(
            modifier = Modifier
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                .fillMaxWidth(1f)
                .defaultMinSize(minHeight = 250.dp),
            value = mainContent.value,
            onValueChange = { textValue -> mainContent.value = textValue },
            placeholder = { Text(text = "어떤 추억이 있었냐요?\n나중에 떠올리고 싶은 추억을 그려보세요 :)") },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(15.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(size = 25.dp)
                )
        )

        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(start = 13.dp, top = 5.dp, end = 14.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.v_cd_title),
                    contentDescription = "CDTittle",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Spacer(Modifier.width(23.dp))
                Text(
                    text = "제목",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFF686868),
                    )
                )
            }
            BasicTextField(
                modifier = Modifier.size(height=18.dp, width=200.dp),
                value = title.value,
                onValueChange = { textValue -> title.value = textValue},
                singleLine = true
            )
        }
        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(start = 13.dp, top = 12.dp, end = 14.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.g_cd_calendar),
                    contentDescription = "CDCalendar",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Spacer(Modifier.width(22.dp))
                Text(
                    text = "방문 날짜",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFF686868),
                    )
                )
            }
            BasicTextField(
                modifier = Modifier.size(height=18.dp, width=200.dp),
                value = date.value,
                onValueChange = { textValue -> date.value = textValue},
                singleLine = true
            )
        }
        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(start = 13.dp, top = 12.dp, end = 14.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.g_cd_location),
                    contentDescription = "CDLocation",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Spacer(Modifier.width(22.dp))
                Text(
                    text = "장소",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFF686868),
                    )
                )
            }
            BasicTextField(
                modifier = Modifier.size(height=18.dp, width=200.dp),
                value = locations.value,
                onValueChange = { textValue -> locations.value = textValue},
                singleLine = true
            )
        }
        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(start = 13.dp, top = 12.dp, end = 14.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.v_cd_who),
                    contentDescription = "CDWho",
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Spacer(Modifier.width(22.dp))
                Text(
                    text = "누구랑",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFF686868),
                    )
                )
            }
            BasicTextField(
                modifier = Modifier.size(height=18.dp, width=200.dp),
                value = friends.value,
                onValueChange = { textValue -> friends.value = textValue},
                singleLine = true
            )
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(15.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(size = 25.dp)
                )
        )
        Text(
            text = "사진",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W600,
                color = Color(0xFF454545),
            ),
            modifier = Modifier.padding(start=13.dp, top=10.dp)
        )

        LazyVerticalGrid( //사진 상자
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 32.dp, bottom = 15.dp)
                .height(200.dp)
                .fillMaxWidth(1f)
                .background(color = Color(0xFFF5F5F5))
                .clickable {
                    getPhotoFromGallery.launch("image/*")
                }
        ) {
            items(selectImages) { uri ->
                if(selectImages.size > 3) {
                    Toast.makeText(context, "사진은 4장 이상 고를 수 없습니다", Toast.LENGTH_LONG).show()
                } else {
                    Image(
                        painter = rememberImagePainter(uri),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp, 8.dp)
                            .size(100.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun CDMiddleSearch(){
    Image(
        painter = painterResource(id = R.drawable.v_cd_location_search),
        contentDescription = "CDSearch",
        modifier = Modifier
            .width(15.dp)
            .height(15.dp)
    )
}

