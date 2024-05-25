<script>
import Boton from './Botones/Boton.vue';
import axios from 'axios';
import PagoModal from './Modales/PagoModal.vue';
export default {
    name: "liquidarDeudas",
    components: {
        Boton,
        PagoModal
    },
    data() {
        return {
            sugerencias: [],
            errorPostSubmit: '',
            cargando: true
        }
    },
    methods: {
        async fetchDeudas() {
            this.cargando = true
            
            const url = import.meta.env.VITE_API + "/api/pagos/usf/getSaldosSimplificadosDeGrupoNew/" + this.grupoId
            const response = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    // debugger
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.sugerencias = response.data
                    }
                    this.cargando = false

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.cargando = false
                }.bind(this));

        },
        liquidarDeuda(deuda){
            this.$refs.pagoModal.setPago(deuda)
            this.$refs.pagoModal.toggleDialog(true)
        }
    },
    mounted() {
        this.fetchDeudas()
    },
    props: {
        grupoId: Number,
        moneda : String
    }
}
</script>

<template>
    <PagoModal ref="pagoModal" @refetch="fetchDeudas(); $emit('refetch')" :grupoId="grupoId" :moneda="moneda"/>
    <div style="margin-top: 18px;">
        <p v-if="sugerencias.length == 0 && !cargando">No hay deudas para liquidar</p>
        <div class="spinner" v-if="cargando">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
        <template v-if="!cargando" v-for="(deuda, i) in sugerencias">
            <v-card @click="liquidarDeuda(deuda)" id="deuda" class="mx-auto"
                style="width: 99%; margin-bottom: 10px; border-width: 1px; border-style:solid; border-color:rgb(202, 202, 202); padding-right: 15px;">
                <div style="display: flex; justify-content:space-between;">
                    <div style="display: flex; align-items: center;">
                        <div
                            style="display:flex; flex-direction: column; align-items: center; margin-top: 13px; margin-left: 15px; margin-bottom: 10px; margin-right: 15px;">
                            <!-- Sin imagen -->
                            <img v-if="deuda.deudor.imagen == null || deuda.deudor.imagen == ''" src="/src/assets/svg/User2.svg"
                                style="width: 45px;margin-top: 0px" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${deuda.deudor.imagen}`" class="roundedd" style="">
                            <div>
                                {{ deuda.deudor.nombre }}
                            </div>
                        </div>
                    </div>
                    <div style="flex: 1; display: flex; flex-direction: column; margin-top: 10px;">
                        <div style="margin-top: 11px;align-self: center; display: flex; align-items: center;">
                            <v-chip
                                style="cursor: pointer;font-size: 16px; margin-left: 8px; margin-right:10px; background-color: var(--color-primario); color: white;">
                                ${{ deuda.monto }}
                            </v-chip>
                        </div>

                        <div class="arrow">
                            <div class="line"></div>
                            <div class="point"></div>
                        </div>
                    </div>

                    <div style="display: flex;">
                        <div
                            style="display:flex; flex-direction: column; align-items: center; margin-top: 13px; margin-left: 22px; margin-bottom: 10px;">
                            <!-- Sin imagen -->
                            <img v-if="deuda.acreedor.imagen == null || deuda.acreedor.imagen == ''" src="/src/assets/svg/User2.svg"
                                style="width: 45px;margin-top: 0px;" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${deuda.acreedor.imagen}`" class="roundedd" style="">
                            <div>
                                {{ deuda.acreedor.nombre }}
                            </div>
                        </div>
                        <!-- <div style="display: flex; flex: 0.001; flex-direction: column; justify-content: center;">

                        <v-btn density="comfortable" variant="text"
                            style="align-self: flex-end; margin: 5px; font-size: 20px"
                            :icon="show == i ? 'mdi-chevron-up' : 'mdi-chevron-down'"
                            @click="show == i ? show = null : show = i"></v-btn>
                    </div> -->
                    </div>
                </div>
            </v-card>
        </template>
        <!-- <p>{{ sugerencias }}</p> -->
        <p style="color: red; margin-top: 15px;"> {{ errorPostSubmit }}</p>
    </div>
</template>

<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 45px;
    width: 45px;
    outline: 1px solid rgb(111, 111, 111);

}

.arrow {
    display: flex;
    align-items: center;
}

.line {
    width: 100%;
    background: rgb(153, 153, 153);
    height: 3px;
}

.point {
    width: 0;
    height: 0;
    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
    border-left: 8px solid rgb(153, 153, 153);
}

#deuda{
    cursor: pointer;
}
#deuda:hover{
    background-color: rgb(234, 234, 234);
}
</style>