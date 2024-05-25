<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import Boton from '../components/Botones/Boton.vue';

export default{
    name: 'cerrarSesion',
    components: {
        Contenedor,
        Boton
    },
    methods:{
        cerrarSesion(){
            localStorage.clear()
            localStorage.setItem("tipoUsuario", "invitado")
            localStorage.setItem("token", "")
            this.$emit('cerrarSesion')
            this.$router.push('/')
        },
        cancelar(){
            this.$router.back()
        }
    },
    mounted(){
        this.$emit("terminoCargar")
    }
}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo>
            Cerrar sesión
        </template>
        <template v-slot:contenido>
            <p style="margin-top: 15px;">Seguro/a que desea cerrar su sesión?</p>
            <div style="margin-top: 20px;">
                <Boton @click="cerrarSesion" tipo="primario2">Cerrar sesión</Boton>
                <Boton @click="cancelar" tipo="secundarioBorde" style="margin-left: 20px;">Cancelar</Boton>
            </div>

        </template>
    </Contenedor>

</template>
