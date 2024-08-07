package com.pixelfusion.accesio_utn.navigation

import CreateScheduleView
import EditScheduleView
import PerfilView
import UserProfileViewModel
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.pixelfusion.accesio_utn.components.SplashScreen
import com.pixelfusion.accesio_utn.components.StartScreen
import com.pixelfusion.accesio_utn.helper.PreferenceHelper
import com.pixelfusion.accesio_utn.view.AboutView
import com.pixelfusion.accesio_utn.view.AccesosListUsersView
import com.pixelfusion.accesio_utn.view.AccessDetailUserView
import com.pixelfusion.accesio_utn.view.AsistenciaListAlumnosView
import com.pixelfusion.accesio_utn.view.CredentialView
import com.pixelfusion.accesio_utn.view.EditarDatosView
import com.pixelfusion.accesio_utn.view.DetailStudentAssistView
import com.pixelfusion.accesio_utn.view.EditImageUserView
//import com.pixelfusion.accesio_utn.view.FormHorariosView
import com.pixelfusion.accesio_utn.view.FormRegisterView
import com.pixelfusion.accesio_utn.view.GenerateQrView
import com.pixelfusion.accesio_utn.view.HistoryMyAssistView
import com.pixelfusion.accesio_utn.view.HistoryPlaceView
import com.pixelfusion.accesio_utn.view.HistoryUserView
import com.pixelfusion.accesio_utn.view.HomeUserView
import com.pixelfusion.accesio_utn.view.HorarioProfesorView
import com.pixelfusion.accesio_utn.view.HorarioView
import com.pixelfusion.accesio_utn.view.ImageCamView
import com.pixelfusion.accesio_utn.view.ImageUserView
import com.pixelfusion.accesio_utn.view.LegalScreen
import com.pixelfusion.accesio_utn.view.ListAssistUsersQrView
import com.pixelfusion.accesio_utn.view.ListQrGenerateView
import com.pixelfusion.accesio_utn.view.LoginScreen
import com.pixelfusion.accesio_utn.view.MyAccessDetailView
import com.pixelfusion.accesio_utn.view.MyAssistDetailView
import com.pixelfusion.accesio_utn.view.MyPlaceDetailView
import com.pixelfusion.accesio_utn.view.QrAsistenciaDetailView
import com.pixelfusion.accesio_utn.view.QrLugarDetailView
import com.pixelfusion.accesio_utn.view.ScanQRAccessView
import com.pixelfusion.accesio_utn.view.ScanQRAssistView
import com.pixelfusion.accesio_utn.view.ScanQRLugarView
import com.pixelfusion.accesio_utn.viewmodel.AccesosListUsersViewModel
import com.pixelfusion.accesio_utn.viewmodel.AccessDetailUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.AsistenciaListAlumnosViewModel
import com.pixelfusion.accesio_utn.viewmodel.CredentialViewModel
import com.pixelfusion.accesio_utn.viewmodel.DetailStudentAssistViewModel
import com.pixelfusion.accesio_utn.viewmodel.FormHorariosViewModel
import com.pixelfusion.accesio_utn.viewmodel.FormRegisterViewModel
import com.pixelfusion.accesio_utn.viewmodel.GenerateQrCodeViewModel
import com.pixelfusion.accesio_utn.viewmodel.HistoryMyAssistViewModel
import com.pixelfusion.accesio_utn.viewmodel.HistoryPlaceViewModel
import com.pixelfusion.accesio_utn.viewmodel.HistoryUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.HomeViewModel
import com.pixelfusion.accesio_utn.viewmodel.HorarioProfesorViewModel
import com.pixelfusion.accesio_utn.viewmodel.ImageUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.ListAssistUsersQrViewModel
import com.pixelfusion.accesio_utn.viewmodel.ListQrGenerateViewModel
import com.pixelfusion.accesio_utn.viewmodel.LoginViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyAccessDetailViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyAssistDetailViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyPlaceDetailViewModel
import com.pixelfusion.accesio_utn.viewmodel.QrAsistenciaDetailViewModel
import com.pixelfusion.accesio_utn.viewmodel.QrLugarDetailViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScanQRAccessViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScanQRAssistViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScanQRLugarViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScannerViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController) // Pass navController to SplashScreen
        }
        composable(AppScreens.MyApp.route) {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = PreferenceHelper(context)
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = when {
        currentUser?.email.isNullOrEmpty() && !prefs.hasSeenStartScreen -> "start_screen"
        currentUser?.email.isNullOrEmpty() -> "login_screen"
        else -> "home_user_view"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("start_screen") {
            StartScreen(navController, context)
        }

        composable("form_register_view") {
            val viewModelU: FormRegisterViewModel = viewModel()
            FormRegisterView(navController, viewModelU)
        }

        /*composable("login_screen") {
            val viewModelUL: LoginViewModel = viewModel()
            LoginScreen(navController, viewModelUL)
        }*/
        composable("login_screen") {
            val viewModelUL: LoginViewModel = viewModel()
            val activity = (LocalContext.current as? Activity)
            LoginScreen(navController, viewModelUL)

            // Manejar el botón de retroceso para salir de la aplicación
            BackHandler {
                activity?.finish()
            }
        }

        composable("legal_screen") {
            LegalScreen(navController, LocalContext.current)
        }

        composable("image_cam_view") {
            val viewModel: ScannerViewModel = viewModel()
            ImageCamView(navController, viewModel)
        }

        composable("image_user_view") {
            val viewModel: ImageUserViewModel = viewModel()
            ImageUserView(navController, viewModel)
        }

        composable("edit_image_user_view") {
            val viewModel: ImageUserViewModel = viewModel()
            EditImageUserView(navController, viewModel)
        }

        composable("home_user_view") {
            val viewModelHome: HomeViewModel = viewModel()
            HomeUserView(navController, viewModelHome)
        }

        composable("credential_view") {
            val viewModelC: CredentialViewModel = viewModel()
            CredentialView(navController, viewModelC)
        }

        composable("horario_view") {
            HorarioView(navController)
        }

        composable("profile_view") {
            val viewModelU: UserProfileViewModel = viewModel()
            PerfilView(navController, viewModelU)
        }

        composable("editar_datos_view") {
            val viewModelU: UserProfileViewModel = viewModel()
            EditarDatosView (navController, viewModelU)
        }

        composable("about_view") {
            AboutView(navController)
        }
        composable("scan_qr_access_view") {
            val viewModelScanQR: ScanQRAccessViewModel = viewModel()
            ScanQRAccessView(navController, viewModelScanQR)
        }

        composable("scan_qr_assist_view") {
            val viewModelScanQRAssist: ScanQRAssistViewModel = viewModel()
            ScanQRAssistView(navController, viewModelScanQRAssist)
        }

        composable("scan_qr_lugar_view") {
            val viewModelScanQRLugar: ScanQRLugarViewModel = viewModel()
            ScanQRLugarView(navController, viewModelScanQRLugar)
        }

        composable("generate_qr_view") {
            val viewModelGenerateQr: GenerateQrCodeViewModel = viewModel()
            GenerateQrView(navController, viewModelGenerateQr)
        }

        composable("history_user_view") {
            val viewModelHistoryUser: HistoryUserViewModel = viewModel()
            HistoryUserView(navController, viewModelHistoryUser)
        }

        composable("lista_mi_qr") {
            val viewModelDataQr: ListQrGenerateViewModel = viewModel()
            ListQrGenerateView(navController, viewModelDataQr)
        }

        composable("qr_lugar_detail/{qrUidLugar}") { backStackEntry ->
            val qrUid = backStackEntry.arguments?.getString("qrUidLugar")
            val viewModelQrLugarDetail: QrLugarDetailViewModel = viewModel()

            QrLugarDetailView(navController, qrUid, viewModelQrLugarDetail)
        }

        composable("qr_asistencia_detail/{qrUidAsistencia}") { backStackEntry ->
            val qrUidAsistencia = backStackEntry.arguments?.getString("qrUidAsistencia")
            val viewModelQrAsistenciaDetail: QrAsistenciaDetailViewModel = viewModel()
            QrAsistenciaDetailView(navController, qrUidAsistencia, viewModelQrAsistenciaDetail)
        }

        composable("history_my_assist_view") {
            val viewModelHistoryMyAssist: HistoryMyAssistViewModel = viewModel()
            HistoryMyAssistView(navController, viewModelHistoryMyAssist)
        }

        composable("asistencia_list_alumnos_view") {
            val viewModelAsistenciaListAlumnos: AsistenciaListAlumnosViewModel = viewModel()
            AsistenciaListAlumnosView(navController, viewModelAsistenciaListAlumnos)
        }

        composable("my_access_detail/{UidMyAccess}") { backStackEntry ->
            val UidMyAccess = backStackEntry.arguments?.getString("UidMyAccess")
            val viewModelHistoryUserDetail: MyAccessDetailViewModel = viewModel()
            val viewModelHistoryUser: HistoryUserViewModel = viewModel()
            MyAccessDetailView(
                navController,
                UidMyAccess,
                viewModelHistoryUserDetail,
                viewModelHistoryUser
            )
        }

        composable("my_assist_detail/{UidMyAssist}") { backStackEntry ->
            val UidMyAssist = backStackEntry.arguments?.getString("UidMyAssist")
            val viewModelHistoryMyAssistDetail: MyAssistDetailViewModel = viewModel()
            val viewModelHistoryMyAssist: HistoryMyAssistViewModel = viewModel()
            MyAssistDetailView(
                navController,
                UidMyAssist,
                viewModelHistoryMyAssistDetail,
                viewModelHistoryMyAssist
            )
        }

        composable("historial_qr_lugar_view") {
            val viewModelHistoryPlace: HistoryPlaceViewModel = viewModel()
            HistoryPlaceView(navController, viewModelHistoryPlace)
        }

        composable("accesos_list_users_view") {
            val viewModelAccessListUsers: AccesosListUsersViewModel = viewModel()
            AccesosListUsersView(navController, viewModelAccessListUsers)
        }

        composable("horario_profesor_view") {
            val viewModelHorarioProfesor: HorarioProfesorViewModel = viewModel()
            HorarioProfesorView(navController, viewModelHorarioProfesor)
        }

        composable("form_horarios_view") {
            val viewModelFormHorarios: FormHorariosViewModel = viewModel()
            //FormHorariosView(navController, viewModelFormHorarios)
        }

        composable("details_access_user/{UidMyAccess}") { backStackEntry ->
            val UidMyAccess = backStackEntry.arguments?.getString("UidMyAccess")
            val AccessDetailUserViewModel: AccessDetailUserViewModel = viewModel()
            val viewModelHistoryUser: HistoryUserViewModel = viewModel()
            AccessDetailUserView(
                navController,
                UidMyAccess,
                AccessDetailUserViewModel,
                viewModelHistoryUser
            )
        }

        composable("my_place_detail/{UidMyPlace}") { backStackEntry ->
            val UidMyPlace = backStackEntry.arguments?.getString("UidMyPlace")
            val MyPlaceDetailViewModel: MyPlaceDetailViewModel = viewModel()
            val viewModelHistoryPlace: HistoryPlaceViewModel = viewModel()
            MyPlaceDetailView(
                navController,
                UidMyPlace,
                MyPlaceDetailViewModel,
                viewModelHistoryPlace
            )
        }

        composable("list_access_users_by_qr/{UidQRAccessUser}") { backStackEntry ->
            val UidQRAccessUser = backStackEntry.arguments?.getString("UidQRAccessUser")
            val ListAssistUsersQrViewModel: ListAssistUsersQrViewModel = viewModel()
            if (UidQRAccessUser != null) {
                ListAssistUsersQrView(navController, UidQRAccessUser, ListAssistUsersQrViewModel)
            } else {
                ListAssistUsersQrView(navController, "", ListAssistUsersQrViewModel)
            }
        }

        composable("detail_student_assist/{UidQrAsistencia}/{UidUser}") { backStackEntry ->
            val uidStudentAsistencia = backStackEntry.arguments?.getString("UidQrAsistencia")
            val uidUserStudent = backStackEntry.arguments?.getString("UidUser")
            val DetailStudentAssistViewModel: DetailStudentAssistViewModel = viewModel()

            if (uidStudentAsistencia != null && uidUserStudent != null) {
                DetailStudentAssistView(
                    navController,
                    uidStudentAsistencia,
                    uidUserStudent,
                    DetailStudentAssistViewModel
                )
            } else {
                // Manejar el caso donde los argumentos son nulos
                Text("Error: No se encontraron detalles de la asistencia.")
            }
        }

        /*
        REPORTES: PARA LOS EMPLEADOS
        * */

        composable("edit_schedule_view") {
            EditScheduleView(navController)
        }

        composable("create_schedule_view") {
            CreateScheduleView(navController)
        }
    }
}
