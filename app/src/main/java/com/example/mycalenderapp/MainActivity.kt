package com.example.mycalenderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalenderapp.ui.theme.MyCalenderAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCalenderAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarApp(
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()
    val memoList by db.memoDao().getAll()
        .collectAsState(initial = emptyList())

    var selectedUid by remember {
        mutableStateOf(Memo(0, "ERROR"))
    }

    var clickedDate by remember {
        mutableStateOf(0)
    }

    val checkList by db.checklistDao().getAll()
        .collectAsState(initial = emptyList())

    var toolAdd by remember {
        mutableStateOf(false)
    }
    var memoButton by remember {
        mutableStateOf(false)
    }
    var checklistButton by remember {
        mutableStateOf(false)
    }
    var voiceMemoButton by remember {
        mutableStateOf(false)
    }
    var memoClick by remember {
        mutableStateOf(false)
    }
    var checklistClick by remember {
        mutableStateOf(false)
    }
    var voiceMemoClick by remember {
        mutableStateOf(false)
    }
    var memoScreen by remember {
        mutableStateOf(false)
    }
    var checklistScreen by remember {
        mutableStateOf(false)
    }

    var checklistEdit by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier.fillMaxSize()) {
        val dataSource = CalendarDataSource()
        var calendarUiModel by remember {
            mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today))
        }
        Header(
            data = calendarUiModel,
            onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            },
            onNextClickListener = { endDate ->
                val finalStartDate = endDate.plusDays(2)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            }
        )

        fun convertLocalDateToInt(dateModel: CalendarUiModel.Date): Int {
            val date = dateModel.date
            return date.year * 10000 + date.monthValue * 100 + date.dayOfMonth
        }

        Content(data = calendarUiModel, onDateClickListener = { date ->
            calendarUiModel = calendarUiModel.copy(
                selectedDate = date,
                visibleDates = calendarUiModel.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )

            clickedDate = convertLocalDateToInt(calendarUiModel.selectedDate)
        })
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.LightGray)
        ) {
            item {
                Text(text = "0000년 0월 0일 D-00 내 생일")
            }
        }
        // Add MemoButton, ChecklistButton, VoiceMemoBUtton
        Row {
            Column(modifier = modifier) {
                Text(text = "여러 기능")
                Row {
                    Tools(addTool = memoButton, text = "메모", clickedAddTool = memoClick) {
                        memoClick = !memoClick
                        checklistClick = false
                        voiceMemoClick = false
                    }
                    Tools(
                        addTool = checklistButton,
                        text = "체크리스트",
                        clickedAddTool = checklistClick
                    ) {
                        checklistClick = !checklistClick
                        memoClick = false
                        voiceMemoClick = false
                    }
                    Tools(
                        addTool = voiceMemoButton,
                        text = "음성메모",
                        clickedAddTool = voiceMemoClick
                    ) {
                        voiceMemoClick = !voiceMemoClick
                        memoClick = false
                        checklistClick = false
                    }
                }
            }
            Button(onClick = { toolAdd = !toolAdd }) {
                Text(text = "추가")
            }
        }
        //메모,체크리스트 버튼 클릭 했을 때 아래에 해당 컨텐츠 표시
        ToolsClick(
            toolClick = memoClick,
            item = {
                memoList.forEach { memo ->
                    //val memo_temp by db.memoDao().loadUid(1)
                    //    .collectAsState(initial = Memo(0,"ERROR"))

//                    val memo_temp = db.memoDao().loadUid(1)
//
//                    memo_temp?.memo
                }
            },
            onClick = { memoScreen = !memoScreen })

        ToolsClick(toolClick = checklistClick, item = {
            checkList.forEach { checklist ->
                Row {
                    Text(
                        text = checklist.checklist ?: "",
                        modifier = Modifier
                            .weight(1f)
                            .strikeThroughText(checklist.isChecked!!),
                        color = if (checklist.isChecked!!) Color.Gray else Color.Black
                    )
                    Checkbox(
                        checked = checklist.isChecked ?: false,
                        onCheckedChange = { newValue ->
                            val updatedChecklist = checklist.copy(isChecked = newValue)
                            scope.launch(Dispatchers.IO) {
                                db.checklistDao().updateChecklist(updatedChecklist)
                            }
                        }
                    )
                }
            }
        }, onClick = { checklistScreen = !checklistScreen })
        ToolsClick(
            toolClick = voiceMemoClick,
            item = { Text(text = "음성메모") },
            onClick = { })
    }
    //메모,체크리스트,음성메모 버튼 추가하는 창
    if (toolAdd) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .background(Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.Cyan)
                        .height(30.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "추가")
                }
                AddTools(addTool = memoButton, text = "메모") {
                    memoButton = !memoButton
                }
                AddTools(addTool = checklistButton, text = "체크리스트") {
                    checklistButton = !checklistButton

                }
                AddTools(addTool = voiceMemoButton, text = "음성 메모") {
                    voiceMemoButton = !voiceMemoButton
                }
                Button(onClick = { toolAdd = !toolAdd }) {
                    Text(text = "닫기")
                }
            }
        }
    }

    if(memoScreen)
    {
        //메모 수정하는 스크린
        MemoScreen(db = db, memoScreen = memoScreen, memoList = memoList, scope = scope, clickedDate = clickedDate)
    }
    //체크리스트 수정하는 스크린
    ChecklistScreen(
        db = db,
        checklistScreen = checklistScreen,
//        checklistEdit = checklistEdit,
        checkList = checkList,
        scope = scope
    )
}

@Composable
fun MemoScreen(db: AppDatabase, memoScreen: Boolean, memoList: List<Memo>, scope: CoroutineScope, clickedDate:Int) {

    if (memoScreen) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .background(Color.LightGray)
            ) {

                var isEditing by remember { mutableStateOf(false) }


                Column {
                    var updatedMemo by remember { mutableStateOf("") }
                    var beforeEditMemo by remember { mutableStateOf("") }

                    scope.launch(Dispatchers.IO) {
                        val memo = db.memoDao().getMemoByUid(clickedDate)
                        withContext(Dispatchers.Main) {
                            beforeEditMemo = memo?.memo ?: ""
                        }
                    }

                    EditableTextComponent(
                        text = updatedMemo,
                        savedtext = beforeEditMemo, // 여기를 수정했습니다.
                        isEditing = isEditing,
                        onTextChange = { newText -> updatedMemo = newText },
                        onEditToggle = { newValue -> isEditing = newValue },
                        onClick = {

                            scope.launch(Dispatchers.IO)
                            {
                                db.memoDao().insertOrUpdateMemo(clickedDate, updatedMemo)
                            }
                            isEditing = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    db: AppDatabase,
    checklistScreen: Boolean,
//    checklistEdit: Boolean,
    checkList: List<Checklist>,
    scope: CoroutineScope
) {
    if (checklistScreen) {
        var checklistEdit by remember {
            mutableStateOf(false)
        }
        var checklistItem by remember {
            mutableStateOf("")
        }
        var checklistAdd by remember {
            mutableStateOf(false)
        }
        var checkBoxClick by remember {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .background(Color.LightGray)
            ) {
                for (index in checkList.indices) {
                    val checklist = checkList[index]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = checklist.checklist ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .strikeThroughText(checklist.isChecked!!),
                            color = if (checklist.isChecked!!) Color.Gray else Color.Black
                        )
                        Checkbox(
                            checked = checklist.isChecked ?: false,
                            onCheckedChange = { isChecked ->
                                val updatedChecklist = checklist.copy(isChecked = isChecked)
                                scope.launch(Dispatchers.IO) {
                                    db.checklistDao().updateChecklist(updatedChecklist)
                                }
                            }
                        )
                    }
                }

                Button(onClick = {
                    checklistAdd = !checklistAdd
                }) {
                    Text(text = "+")
                }
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "닫기")
            }
        }
        if (checklistAdd) {

            Column {
                Row {
                    TextField(value = checklistItem, onValueChange = { checklistItem = it })
                    Button(onClick = {
                        if (checklistItem.isNotBlank()) {

                            val newChecklist = Checklist(
                                checklist = checklistItem,
                                isChecked = checkBoxClick
                            )
                            scope.launch(Dispatchers.IO) {
                                db.checklistDao().insertAll(newChecklist)
                            }
                            checklistItem = ""
                        }
                    }) {
                        Text(text = "추가")
                    }
                }
                for (index in checkList.indices) {
                    val checklist = checkList[index]
                    var updatedChecklist by remember { mutableStateOf(checklist.checklist ?: "") }

                    Row {
                        if (checklistEdit) {
                            TextField(
                                value = updatedChecklist,
                                onValueChange = { updatedChecklist = it }
                            )
                            Button(onClick = {
                                val updatedUser = checklist.copy(checklist = updatedChecklist)
                                scope.launch(Dispatchers.IO) {
                                    db.checklistDao().updateChecklist(updatedUser)
                                }
                                checklistEdit = false
                            }) {
                                Text(text = "저장")
                            }
                        } else {
                            Text(text = checklist.checklist ?: "")
                            Button(onClick = {
                                updatedChecklist = checklist.checklist ?: ""
                                checklistEdit = !checklistEdit
                            }) {
                                Text(text = "수정")
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun Tools(addTool: Boolean, clickedAddTool: Boolean, text: String, onClick: () -> Unit) {
    Row {
        if (addTool) {
            Button(
                onClick = onClick, colors = ButtonDefaults.buttonColors(
                    if (clickedAddTool) Color.Green else Color.Blue,
                    contentColor = Color.White
                )
            ) {
                Text(text = text)
            }
        }
    }
}

@Composable
fun AddTools(addTool: Boolean, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            if (addTool) Color.Green else Color.Blue,
            contentColor = Color.White
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun ToolsClick(toolClick: Boolean, item: @Composable () -> Unit, onClick: () -> Unit) {
    if (toolClick) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .background(Color.LightGray)
                .height(200.dp)
                .clickable { onClick() }
        ) {
            item {
                item()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTextComponent(
    text: String,
    savedtext: String,
    isEditing: Boolean,
    onTextChange: (String) -> Unit,
    onEditToggle: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    if (isEditing) {
        Column {
            TextField(
                value = text,
                onValueChange = { newText -> onTextChange(newText) },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                onEditToggle(false)
                onClick()
            }) {
                Text(text = "저장")
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onEditToggle(true) }
        ) {
            Text(text = savedtext, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
        }
    }
}

fun Modifier.strikeThroughText(strike: Boolean): Modifier = this.then(
    if (strike) {
        Modifier.drawWithContent {
            drawContent()
            drawLine(
                color = Color.Black,
                strokeWidth = 2f,
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f)
            )
        }
    } else {
        Modifier.width(20.dp)
    }
)

@Composable
fun Header(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
    val currentYearMonth = YearMonth.now()
    val currentMonth = remember { mutableStateOf(currentYearMonth) }
    Column(modifier = Modifier.padding(10.dp)) {
        Row {
            Text(
                text =
                currentMonth.value.format(DateTimeFormatter.ofPattern("yyyy년 MMMM", Locale("ko"))),
                modifier = Modifier.weight(1f),
                fontSize = 30.sp
            )

            IconButton(onClick = {
                onPrevClickListener(data.startDate.date)
                val newMonth = currentMonth.value.minusMonths(1)
                currentMonth.value = newMonth
            }) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Back"
                )
            }
            IconButton(onClick = {
                onNextClickListener(data.endDate.date)
                val newMonth = currentMonth.value.plusMonths(1)
                currentMonth.value = newMonth
            }) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Next"
                )
            }
        }
    }

}

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit
) {
    Column {
        repeat(6) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val startIndex = rowIndex * 7
                val endIndex = minOf(startIndex + 7, data.visibleDates.size)
                for (i in startIndex until endIndex) {
                    val date = data.visibleDates[i]
                    ContentItem(date = date, onDateClickListener)
                }
            }
        }
        Text(
            data.selectedDate.date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            )
        )
    }
}

@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit
) {
    val currentYearMonth = YearMonth.now()
    val textColor =
        if (date.date.year == currentYearMonth.year && date.date.month == currentYearMonth.month) {
            if (date.date.dayOfWeek == DayOfWeek.SUNDAY) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.primary
            }
        } else {
            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        }
    val backgroundColor = if (date.isToday && !date.isSelected) {
        Color.LightGray
    } else if (date.isSelected) {
        Color(0xFFF781F3).copy(alpha = 0.7f)
    } else {
        Color.White
    }
    Card(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .width(35.dp)
            .clickable {
                onClickListener(date)
            },
        colors = CardDefaults.cardColors(
            backgroundColor
        ),
    ) {
        Column(
            modifier = Modifier
                .width(35.dp)
                .height(48.dp)

        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    CalendarApp(
        modifier = Modifier.padding(0.dp)
    )
}