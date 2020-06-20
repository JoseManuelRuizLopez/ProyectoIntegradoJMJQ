package com.example.proyectointegradojmjq;

import android.content.Context;
import android.view.Gravity;
import android.widget.DatePicker;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
import androidx.test.espresso.assertion.ViewAssertions;


import com.rd.draw.drawer.Drawer;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {


    Context appContext;

    @Rule
    public ActivityTestRule<Login> activityRule = new ActivityTestRule<>(Login.class);

    Login login;


    @Before
    public void createLogin() {

        login = activityRule.getActivity();

    }



    @Test
    public void A_pruebaLogin()
    {
        onView(withId(R.id.txtUsuarioLogin)).perform(typeText("jose"));
        onView(withId(R.id.txtClaveLogin)).perform(typeText("jose"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.swMantenerSesion)).perform(click());

        onView(withId(R.id.txtClaveLogin)).check(matches(withText("jose")));
        onView(withId(R.id.txtUsuarioLogin)).check(matches(withText("jose")));

        onView(withId(R.id.btnEntrarLogin)).perform(click());
    }

    @Test
    public void B_pruebaEnviarMensaje()
    {
        onView(withId(R.id.txtUsuarioLogin)).perform(typeText("jose"));
        onView(withId(R.id.txtClaveLogin)).perform(typeText("jose"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.swMantenerSesion)).perform(click());
        onView(withId(R.id.btnEntrarLogin)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).perform(click());
        onView(withId(R.id.btnChatearVP)).perform(click());

        onView(withId(R.id.txtMensajeEnviar)).perform(typeText("WHITE BOX"));
        onView(withId(R.id.txtMensajeEnviar)).check(matches(withText("WHITE BOX")));
        onView(withId(R.id.btnEnviar)).perform(click(), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.txtMensajeEnviar)).perform(typeText("WHITE BOX 2"));
        onView(withId(R.id.txtMensajeEnviar)).check(matches(withText("WHITE BOX 2")));
        onView(withId(R.id.btnEnviar)).perform(click(), ViewActions.closeSoftKeyboard());
    }


    @Test
    public void C_pruebaRecuperarContrasena()
    {
        onView(withId(R.id.lblRecordarClave)).perform(click());

        onView(withId(R.id.txtCorreoRC)).perform(typeText("josemanuelruizlopez97@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.txtCorreoRC)).check(matches(withText("josemanuelruizlopez97@gmail.com")));

        onView(withId(R.id.btnEnviarRC)).perform(click());
    }

    @Test
    public void D_pruebaConfiguracionPerfil()
    {

        onView(withId(R.id.txtUsuarioLogin)).perform(typeText("jose"));
        onView(withId(R.id.txtClaveLogin)).perform(typeText("jose"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.txtUsuarioLogin)).check(matches(withText("jose")));
        onView(withId(R.id.txtClaveLogin)).check(matches(withText("jose")));

        onView(withId(R.id.swMantenerSesion)).perform(click());

        onView(withId(R.id.btnEntrarLogin)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Configuración")).perform(click());

        onView(withId(R.id.txtEmailConf)).perform(clearText(), typeText("julitoelmaras@gmail.com"));
        onView(withId(R.id.txtEmailConf)).check(matches(withText("julitoelmaras@gmail.com")));

        onView(withId(R.id.txtClaveConf)).perform(typeText("josepass"));
        onView(withId(R.id.txtClaveConf)).check(matches(withText("josepass")));

        onView(withId(R.id.txtClaveConf2)).perform(typeText("josepass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.txtClaveConf2)).check(matches(withText("josepass")));

        onView(withId(R.id.btnGuardarCambiosConf)).perform(click());

    }




    @Test
    public void E_pruebaCerraSesion()
    {

        onView(withId(R.id.txtUsuarioLogin)).perform(typeText("jose"));
        onView(withId(R.id.txtClaveLogin)).perform(typeText("josepass"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.txtUsuarioLogin)).check(matches(withText("jose")));
        onView(withId(R.id.txtClaveLogin)).check(matches(withText("josepass")));

        onView(withId(R.id.swMantenerSesion)).perform(click());

        onView(withId(R.id.btnEntrarLogin)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Cerrar Sesión")).perform(click());

        onView(withText("Aceptar")).perform(click());

    }

    @Test
    public void F_PruebaCrearUsuario()
    {
        onView(withId(R.id.btnRegistrarLogin)).perform(click());

        onView(withId(R.id.txtNombreUsuarioCU)).perform(typeText("Juanito"));
        onView(withId(R.id.txtNombreUsuarioCU)).check(matches(withText("Juanito")));
        onView(withId(R.id.txtClaveUsuarioCU)).perform(typeText("clave"));
        onView(withId(R.id.txtClaveUsuarioCU)).check(matches(withText("clave")));
        onView(withId(R.id.txtRepetirClaveUsuarioCU)).perform(typeText("clave"));
        onView(withId(R.id.txtRepetirClaveUsuarioCU)).check(matches(withText("clave")));
        onView(withId(R.id.txtEmailUsuarioCU)).perform(typeText("juan@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.txtEmailUsuarioCU)).check(matches(withText("juan@gmail.com")));

        onView(withId(R.id.btnCrearUsuarioCU)).perform(click());
        onView(withId(R.id.btnSiguienteWelcomeF1)).perform(click());

        onView(withId(R.id.txtNombreRealB)).perform(typeText("Juanito García Pérez"));
        onView(withId(R.id.txtNombreRealB)).check(matches(withText("Juanito García Pérez")));
        onView(withId(R.id.txtFechaNacimientoB)).perform(typeText("08/01/1998"));
        onView(withId(R.id.txtFechaNacimientoB)).check(matches(withText("08/01/1998")));
        onView(withId(R.id.txtDescripcionMiP)).perform(typeText("Soy juanito el mas chulo jaja asi es"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btnSiguienteWelcomeF2)).perform(click());

        onView(withId(R.id.spnGeneroMiP)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.spnGeneroMiP)).check(matches(withSpinnerText(containsString("Masculino"))));
        onView(withId(R.id.rBoton1WelcomeF3)).perform(click());
        onView(withId(R.id.seekBarMiPerfil)).perform(click());

        onView(withId(R.id.btnSiguienteWelcomeF3)).perform(click());
        onView(withId(R.id.btnSiguienteWelcomeF4)).perform(click());

    }


    @Test
    public void G_pruebaBorrarPerfil()
    {

        onView(withId(R.id.txtUsuarioLogin)).perform(typeText("juanito"));
        onView(withId(R.id.txtClaveLogin)).perform(typeText("clave"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.txtUsuarioLogin)).check(matches(withText("juanito")));
        onView(withId(R.id.txtClaveLogin)).check(matches(withText("clave")));

        onView(withId(R.id.swMantenerSesion)).perform(click());

        onView(withId(R.id.btnEntrarLogin)).perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Configuración")).perform(click());

        onView(withId(R.id.btnBorrarPerfilConf)).perform(click());

        onView(withText("Aceptar")).perform(click());

    }
}