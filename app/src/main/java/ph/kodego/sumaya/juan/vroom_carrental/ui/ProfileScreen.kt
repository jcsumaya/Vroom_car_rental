import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64.encodeToString
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.core.content.edit
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import ph.kodego.sumaya.juan.vroom_carrental.ui.TopMenu
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ph.kodego.sumaya.juan.vroom_carrental.model.User
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography
import ph.kodego.sumaya.juan.vroom_carrental.theme.White
import ph.kodego.sumaya.juan.vroom_carrental.ui.BottomMenu
import ph.kodego.sumaya.juan.vroom_carrental.ui.getBottomMenuItems

val currentUser = User("0","Name", "Email", "Phone")

@Composable
fun ProfileScreen(navController: NavController) {
    Box(modifier = Modifier
        .background(White)
        .fillMaxSize())
    {
        TopMenu(navController)

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePicture(user = currentUser)
            UserInfo(user = currentUser)
        }
        BottomMenu(items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController)
    }
}

@Composable
fun ProfilePicture(user: User) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(
        "profile_picture", Context.MODE_PRIVATE)

    var profilePicture by remember { mutableStateOf<Bitmap?>(null) }

    fun encodeToBase64(image: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeBase64(encodedImage: String): Bitmap? {
        val decodedByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

    LaunchedEffect(Unit) {
        val encodedImage = sharedPreferences.getString(user.id, null)
        if (encodedImage != null) {
            profilePicture = decodeBase64(encodedImage)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            profilePicture = BitmapFactory.decodeStream(inputStream)

            val encodedImage = encodeToBase64(profilePicture!!)
            sharedPreferences.edit {
                putString(user.id, encodedImage)
            }
        }
    }

    Box(modifier = Modifier
        .size(150.dp)
        .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (profilePicture == null) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile picture",
                modifier = Modifier.size(80.dp)
            )
        } else {
            Image(
                painter = rememberImagePainter(data = profilePicture),
                contentDescription = "Profile picture",
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(20.dp)
                .background(
                    color = DarkBlue,
                    shape = CircleShape
                ),
            content = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Picture",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

@Composable
fun UserInfo(user: User) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE)

    var isEditMode by remember { mutableStateOf(false) }
    val name = remember { mutableStateOf(sharedPreferences.getString("name", user.name)!!) }
    val email = remember { mutableStateOf(sharedPreferences.getString("email", user.email)!!) }
    val phone = remember { mutableStateOf(sharedPreferences.getString("phone", user.phone)!!) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Personal Information",
            style = Typography.h4,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isEditMode) {
            TextField(
                value = name.value,
                onValueChange = { name.value = limitCharacters(it, 25)},
                label = { Text("Name") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                maxLines = 1
            )
            TextField(
                value = email.value,
                onValueChange = { email.value = limitCharacters(it, 25)},
                label = { Text("Email") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                maxLines = 1
            )
            TextField(
                value = phone.value,
                onValueChange = { phone.value = limitCharacters(it, 18)},
                label = { Text("Phone") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                maxLines = 1
            )
        } else {
            Text(
                text = "Name:   ${name.value}",
                style = Typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Email:   ${email.value}",
                style = Typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Phone:   ${phone.value}",
                style = Typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Button(
            onClick = {
                if (isEditMode) {
                    sharedPreferences.edit()
                        .putString("name", name.value)
                        .putString("email", email.value)
                        .putString("phone", phone.value)
                        .apply()
                    isEditMode = false
                } else {
                    isEditMode = true
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(DarkBlue)
        ) {
            if (isEditMode) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = "Save")
            } else {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = "Edit Profile")
            }
        }
    }
}

fun limitCharacters(input: String, limit: Int): String {
    return input.take(limit)
}

//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen()
//}