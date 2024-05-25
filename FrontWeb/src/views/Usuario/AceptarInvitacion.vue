<script>
import Boton from '../../components/Botones/Boton.vue';
import Contenedor from '../../components/Contenedores/Contenedor.vue';
import axios from 'axios';

export default {
    name: 'aceptarInvitacion',
    components: {
        Boton,
        Contenedor
    },
    data() {
        return {
            // email: '',
            // contrasena: '',
            // confContrasena: '',
            // errorPostSubmit: '',
            errorToken: '',
            tokenVerificado: false,
            infoGrupo: {}
        }
    },
    methods: {
        async aceptarInvitacion() {
            const data = this.$route.params.token
            //debugger
            const response = await axios.post(import.meta.env.VITE_API + "/api/usuarios/usf/aceptarInvitacion", data,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                }
            })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {
                        this.errorToken = "No se pudo aceptar la invitacion"
                    } else {
                        const rutaGrupo = "/grupo/" + this.infoGrupo.id
                        this.$router.push(rutaGrupo)
                    }
                    this.$router.push(redirect)
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    },
    async mounted() {
        if(this.$route.meta.invitado){
            this.$router.push({path:'/iniciarSesion', query: { t: this.$route.params.token }})
        }else{
            
        }
        
        const urlString = import.meta.env.VITE_API + "/api/grupos/usf/obtenerInfoGrupoPorToken/" + this.$route.params.token
        const response = await axios.get(urlString,
            {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                }
            })
            .then(function (response) {
                if (response.data.estatus != null && response.data.estatus == false) {
                    this.errorToken = "La invitacion ha expirado :("
                } else {
                    this.infoGrupo = response.data
                    this.tokenVerificado = true
                }

            }.bind(this))
            .catch(function (error) {
                this.errorToken = "No se pudo realizar esta accion"
            }.bind(this));

        this.$emit("terminoCargar")
    },
}
</script>

<template>
    <Contenedor>
        <template v-slot:contenido>
            <div v-if="tokenVerificado">
                <div style="display:flex; flex-direction: column; align-items: center;">
                    <div>
                        <!-- Sin imagen -->
                        <img v-if="infoGrupo.imagen == null || infoGrupo.imagen ==''" src='/src/assets/images/Grupo.png'
                            style="width: 140px;margin-top: 0px;" class="squared" />
                        <!-- Con imagen -->
                        <img v-else :src="`data:image/png;base64,${infoGrupo.imagen}`" class="squared">
                    </div>
                    <h2>
                        {{ infoGrupo.nombre }}
                    </h2>

                    <div style="display: flex; gap: 6px; margin-top: 5px;">
                        <template v-for="(integrante, i) in infoGrupo.participantes">
                            <v-chip style="font-size: 11px; padding-left: 4px;" variant="outlined">
                                <template v-slot:prepend>
                                    <!-- Sin imagen -->
                                    <img v-if="integrante.imagen == null || integrante.imagen ==''" src="/src/assets/svg/User2.svg"
                                        style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                    <!-- Con imagen -->
                                    <img v-else :src="`data:image/png;base64,${integrante.imagen}`" class="roundedd"
                                        style="margin-right: 8px;">
                                </template>
                                {{ integrante.nombre }}
                            </v-chip>
                        </template>
                    </div>

                </div>
                <v-divider style="margin-top: 15px; margin-bottom: 15px;"></v-divider>
                <p>Deseas unirte a este grupo?</p>
                <Boton @click="aceptarInvitacion" tipo="primario2" style="margin-top: 15px;">Aceptar invitación</Boton>
            </div>
            
            <p v-if="tokenVerificado" id="errorControl" style="margin-top: 15px;">{{ errorToken }}</p>
            <p v-if="!tokenVerificado" id="errorControl">{{ errorToken }}</p>
        </template>

    </Contenedor>
</template>


<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 25px;
    width: 25px;
    outline: 1px solid rgb(111, 111, 111);

}
</style>