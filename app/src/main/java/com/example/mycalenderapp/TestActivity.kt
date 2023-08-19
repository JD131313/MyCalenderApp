//package com.example.mycalenderapp
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.Button
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//
//class TestActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun afa() {
//    var checklistAdd by remember {
//        mutableStateOf(false)
//    }
//    var checklistEdit by remember {
//        mutableStateOf(false)
//    }
//    var checklistItem by remember {
//        mutableStateOf("")
//    }
//    var newChecklistItem by remember {
//        mutableStateOf("")
//    }
//    var checklistItems by remember {
//        mutableStateOf(mutableListOf<String>())
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .width(300.dp)
//                .height(400.dp)
//                .background(Color.LightGray)
//        ) {
//            checklistItems.forEachIndexed { index, item ->
//                Row {
//                    Text(text = item)
//
//                }
//            }
//            Button(onClick = { checklistAdd = !checklistAdd }) {
//                Text(text = "+")
//            }
//        }
//        Button(onClick = { /*TODO*/ }) {
//            Text(text = "닫기")
//        }
//        val (checked1, setChecked1) = remember { mutableStateOf(false) }
//        val (checked2, setChecked2) = remember { mutableStateOf(false) }
//        val (checked3, setChecked3) = remember { mutableStateOf(false) }
//        val allChecked = (checked1 && checked2 && checked3)
//        Column(verticalArrangement = Arrangement.Center) {
//            CheckBoxRow(
//                text = "전체 동의",
//                value = allChecked,
//                onClick = {
//                    if (allChecked) {
//                        setChecked1(false)
//                        setChecked2(false)
//                        setChecked3(false)
//                    } else {
//                        setChecked1(true)
//                        setChecked2(true)
//                        setChecked3(true)
//                    }
//                },
//            )
//            CheckBoxRow(text = "동의1", value = checked1, onClick = { setChecked1(!checked1) })
//            CheckBoxRow(text = "동의2", value = checked2, onClick = { setChecked2(!checked2) })
//            CheckBoxRow(text = "동의3", value = checked3, onClick = { setChecked3(!checked3) })
//        }
//    }
//    Column {
//        Row {
//            TextField(value = checklistItem, onValueChange = { checklistItem = it })
//            Button(onClick = {
//                if (checklistItem.isNotBlank()) {
//                    newChecklistItem = checklistItem
//                    checklistItems.add(newChecklistItem)
//                    checklistItem = ""
//                }
//            }) {
//                Text(text = "추가")
//            }
//        }
//        checklistItems.forEachIndexed { index, item ->
//            Row {
//                if (checklistEdit) {
//                    TextField(value = item, onValueChange = { checklistItems[index] = it })
//                } else {
//                    Text(text = item)
//                }
//                Button(onClick = { checklistEdit = !checklistEdit }) {
//                    Text(text = "수정")
//                }
//            }
//        }
//    }
//}
//
//체크리스트 한번에 작동 구현법
//Column(verticalArrangement = Arrangement.Center) {
//    CheckBoxRow(
//        text = "전체 동의",
//        value = allChecked,
//        onClick = {
//            if (allChecked) {
//                setChecked1(false)
//                setChecked2(false)
//                setChecked3(false)
//            } else {
//                setChecked1(true)
//                setChecked2(true)
//                setChecked3(true)
//            }
//        },
//    )
//    CheckBoxRow(
//        text = "동의1",
//        value = checked1,
//        onClick = { setChecked1(!checked1) })
//    CheckBoxRow(
//        text = "동의2",
//        value = checked2,
//        onClick = { setChecked2(!checked2) })
//    CheckBoxRow(
//        text = "동의3",
//        value = checked3,
//        onClick = { setChecked3(!checked3) })
//}
//@Preview
//@Composable
//fun afaf() {
//
//}