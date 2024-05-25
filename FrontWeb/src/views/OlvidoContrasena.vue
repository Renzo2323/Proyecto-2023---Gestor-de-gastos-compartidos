<script>
import Contenedor from '../components/Contenedores/Contenedor.vue';
import FormularioOlvidoContrasena from '../components/Formularios/FormularioOlvidoContrasena.vue';

export default{
    name: 'olvidoContrasena',
    components: {
        Contenedor,
        FormularioOlvidoContrasena
    },
    data() {
        return {
            enviado: false
        }
    },
    methods: {
        mostrarMensaje(){
            
        }
    },
    mounted(){
        this.$emit("terminoCargar")
    }
}
</script>

<template>
    <Contenedor>
        <template v-slot:titulo v-if="!enviado">
            Recupera tu contraseña
        </template>
        <template v-slot:titulo v-if="enviado">
            Correo enviado
        </template>
        <template v-slot:contenido>
            <FormularioOlvidoContrasena v-if="!enviado" @envioCorreo="enviado = true"/>
            <p v-if="enviado">Se ha enviado el enlace de recuperación, en caso de no visualizar el correo revisar la sección de spam.</p>
        </template>
    </Contenedor>

</template>