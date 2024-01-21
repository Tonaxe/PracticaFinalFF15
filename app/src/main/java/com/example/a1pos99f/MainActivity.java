package com.example.a1pos99f;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a1pos99f.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Configurar la barra de herramientas y la navegación
        setupNavigation();

        // Reemplazar el fragmento con animaciones de desvanecimiento
        replaceFragmentWithFade(new LogoFragment());

        new Handler().postDelayed(() -> replaceFragmentWithFade(new LogInFragment()), 2000);
    }

    private void setupNavigation() {
        // Configuración del NavController
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Obtén el grafo de navegación
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(R.id.logInFragment, R.id.homeFragment,
                            R.id.drawer1Fragment, R.id.drawer2Fragment, R.id.drawer3Fragment)
                            .setOpenableLayout(binding.drawerLayout)
                            .build();

            // Configurar la barra de herramientas y el Drawer con el NavController
            NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupWithNavController(binding.bottomNavView, navController);

            // Manejar eventos de navegación desde el Drawer
            binding.navView.setNavigationItemSelectedListener(item -> {
                item.setChecked(true);
                binding.drawerLayout.closeDrawers();

                int destinationId = item.getItemId();
                navController.navigate(destinationId);

                return true;
            });

            // Agregar un listener para manejar cambios de destino
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.logInFragment) {
                    // Ocultar el Toolbar y el BottomNavigationView en el fragmento de login
                    binding.toolbar.setVisibility(View.GONE);
                    binding.bottomNavView.setVisibility(View.GONE);
                } else {
                    // Mostrar el Toolbar y el BottomNavigationView en otros fragmentos
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.bottomNavView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void replaceFragmentWithFade(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Establece las animaciones de desvanecimiento
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Manejar el evento Up (flecha hacia atrás) con el NavController
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp();
    }
}
