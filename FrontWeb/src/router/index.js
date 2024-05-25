import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router'
import Principal from '../views/Principal.vue'
import axios from 'axios';

let porInvitacion = false
let tokenInvitacion = null

async function autenticarRuta(to, from, next) {
  
  //Aqui debe confirmar que el usuario esta autenticado
  const response = await axios.get(import.meta.env.VITE_API + "/api/usuarios/all/rol",
    {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      }
    })
    .then(function (response) {
      if (response.data.mensaje == "Usuario") localStorage.setItem("tipoUsuario", "usuario")
      else if (response.data.mensaje == "Administrador") localStorage.setItem("tipoUsuario", "administrador")
      else localStorage.setItem("tipoUsuario", "invitado")


    }.bind(this))
    .catch(function (error) {
      //debugger
    }.bind(this));
  //Aqui confirma que tiene el rol necesario
  if (localStorage.tipoUsuario && to.meta.roles.includes(localStorage.tipoUsuario)) {
    
    next()
  } else {
    
    next({ path: '/SinAcceso' })
  }

}

async function autenticarRutaAceptarInvitacion(to, from, next){
  
  //Aqui debe confirmar que el usuario esta autenticado
  const response = await axios.get(import.meta.env.VITE_API + "/api/usuarios/all/rol",
    {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      }
    })
    .then(function (response) {
      if (response.data.mensaje == "Usuario") localStorage.setItem("tipoUsuario", "usuario")
      else if (response.data.mensaje == "Administrador") localStorage.setItem("tipoUsuario", "administrador")
      else localStorage.setItem("tipoUsuario", "invitado")


    }.bind(this))
    .catch(function (error) {
      //debugger
    }.bind(this));
  //Aqui confirma que tiene el rol necesario
  if (localStorage.tipoUsuario && to.meta.roles.includes(localStorage.tipoUsuario)) {
    
    next()
  } else {
    
    if(localStorage.tipoUsuario == "invitado"){
      
      
      porInvitacion = true
      tokenInvitacion = to.params.token
      to.meta.invitado = true
      next()
    }else{
      next({ path: '/SinAcceso' })
    }
    
  }

}

function redirigirPrincipal(to, from, next) {
  //En lugar de ir a la pagina principal, va a una especifica de usuario o administrador
  if (localStorage.tipoUsuario && localStorage.tipoUsuario == 'invitado') {
    
    next()
  } else if (localStorage.tipoUsuario && localStorage.tipoUsuario == 'usuario') {
    
    next({ path: '/PrincipalUsuario' })
  } else if (localStorage.tipoUsuario && localStorage.tipoUsuario == 'administrador') {
    
    next({ path: '/PrincipalAdministrador' })
  }

}


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      meta: {
        mostrarLeftBar: false,
        roles: ['invitado']
      },
      beforeEnter: redirigirPrincipal,
      component: Principal,
    },
    {
      path: '/AjustesAdministrador',
      name: 'ajustesAdministrador',
      meta: {
        mostrarLeftBar: true,
        roles: ['administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Administrador/AjustesAdministrador.vue')
    },
    {
      path: '/PrincipalUsuario',
      name: 'principalUsuario',
      meta: {
        mostrarLeftBar: true,
        roles: ['usuario']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Usuario/PrincipalUsuario.vue')
    },
    {
      path: '/PrincipalAdministrador',
      name: 'principalAdministrador',
      meta: {
        mostrarLeftBar: true,
        roles: ['administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Administrador/PrincipalAdministrador.vue')
    },
    {
      path: '/iniciarSesion',
      name: 'iniciarSesion',
      meta: {
        mostrarLeftBar: false,
        roles: ['invitado'],
        porInvitacion: porInvitacion,
        tokenInvitacion: tokenInvitacion
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/IniciarSesion.vue')
    },
    {
      path: '/registro',
      name: 'registro',
      meta: {
        mostrarLeftBar: false,
        roles: ['invitado']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Registro.vue')
    },
    {
      path: '/olvidoContrasena',
      name: 'olvidoContrasena',
      meta: {
        mostrarLeftBar: false,
        roles: ['invitado']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/OlvidoContrasena.vue')
    },
    {
      path: '/recuperarContrasena/:token',
      name: 'recuperarContrasena',
      meta: {
        mostrarLeftBar: false,
        roles: ['invitado']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/RecuperarContrasena.vue')
    },
    {
      path: '/cambioContrasena',
      name: 'cambioContrasena',
      meta: {
        mostrarLeftBar: false,
        roles: ['usuario', 'administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/CambioContrasena.vue')
    },
    {
      path: '/cerrarSesion',
      name: 'cerrarSesion',
      meta: {
        mostrarLeftBar: false,
        roles: ['usuario', 'administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/CerrarSesion.vue')
    },
    {
      path: '/perfil',
      name: 'perfil',
      meta: {
        mostrarLeftBar: true,
        roles: ['usuario', 'administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Perfil.vue')
    },
    //ADMINISTRADOR
    {
      path: '/gestionUsuarios',
      name: 'gestionUsuarios',
      meta: {
        mostrarLeftBar: true,
        roles: ['administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Administrador/GestionUsuarios.vue')
    },
    {
      path: '/gestionGrupos',
      name: 'gestionGrupos',
      meta: {
        mostrarLeftBar: true,
        roles: ['administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Administrador/GestionGrupos.vue')
    },
    {
      path: '/gestionCategorias',
      name: 'gestionCategorias',
      meta: {
        mostrarLeftBar: true,
        roles: ['administrador']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Administrador/GestionCategorias.vue')
    },
    //USUARIO
    {
      path: '/crearGrupo',
      name: 'crearGrupo',
      meta: {
        mostrarLeftBar: false,
        roles: ['usuario']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Usuario/CrearGrupo.vue')
    },
    {
      path: '/grupo/:id',
      name: 'paginaGrupo',
      meta: {
        mostrarLeftBar: true,
        roles: ['usuario']
      },
      beforeEnter: autenticarRuta,
      component: () => import('../views/Usuario/PaginaGrupo.vue')
    },
    {
      path: '/AceptarInvitacion/:token',
      name: 'aceptarInvitacion',
      meta: {
        mostrarLeftBar: false,
        roles: ['usuario'],
        invitado: false
      },
      beforeEnter: autenticarRutaAceptarInvitacion,
      component: () => import('../views/Usuario/AceptarInvitacion.vue')
    },
    {//
      path: '/SinAcceso',
      name: 'sinAcceso',
      meta: {
        mostrarLeftBar: false,
        roles: ['todos']
      },
      component: () => import('../views/SinAcceso.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      meta: {
        mostrarLeftBar: false,
        roles: ['todos']
      },
      component: () => import('../views/PaginaNoEncontrada.vue')
    }
  ]
})

router.resolve({
  name: 'not-found',
  params: { pathMatch: ['not', 'found'] }
}).href

export default router
