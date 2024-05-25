<script>
import Boton from '../Botones/Boton.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue'
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';


export default {
    name: 'pagoModal',
    components: {
        Boton,
        ContenedorCerrable,
        Field
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
            pago: {},
            monto: 0,
            montoTotal: 0,
            nota: '',
            errorPago: '',
            procesando: false
        }
    },
    emits: ['refetch', ],
    methods: {
        toggleDialog(valor) {
            this.errorPago = ""
            if (valor) {
                this.dialog = true
                this.creado = false
            } else {
                this.dialog = false
            }

        },
        setPago(pago) {
            this.pago = pago
            this.monto = this.pago.monto
            this.montoTotal = this.pago.monto
        },
        async marcarComoPagado() {
            if (this.monto > this.montoTotal) {
                this.errorPago = "Debe pagar lo que debe o menos"
                return
            }
            if (this.monto < 0) {
                this.errorPago = "Ingrese un valor positivo"
                return
            }
            if (this.monto == 0) {
                this.errorPago = "Debe pagar algo"
                return
            }
            this.errorPago = ""

            this.procesando = true
            const data = {
                "monto": this.monto,
                "moneda": this.moneda,
                "metodoPago": "PAGO_EXTERNO",
                "nota": this.nota,
                "grupoId": this.grupoId,
                "avatarPagador": this.pago.deudor.id,
                "avatarReceptor": this.pago.acreedor.id
            }


            const response = await axios.post(import.meta.env.VITE_API + "/api/pagos/usf/nuevoPago", data,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    // debugger
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.$emit('refetch')
                        this.creado = true
                    }
                    this.procesando = false
                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));

            // 

        }
    },
    computed: {
        signoDinero() {
            switch (this.moneda) {
                case "USD":
                    return "$"
                case "UYU":
                    return "$U"
                case "ARS":
                    return "$"
                case "EUR":
                    return "€"
                case "GBP":
                    return "£"
                case "JPY":
                    return "¥"
            }
        },
    },
    props: {
        moneda: String,
        grupoId: Number
    },
    mounted() {
        this.errorPago = ""
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <template v-if="!creado">
            <ContenedorCerrable style="width: 532px; " @cerrar="toggleDialog(false)">
                <template v-slot:contenido>
                    <div style="display: flex; justify-content:space-between;">
                        <div style="display: flex; align-items: center;">
                            <div
                                style="display:flex; flex-direction: column; align-items: center; margin-top: 13px; margin-left: 15px; margin-bottom: 10px; margin-right: 15px;">
                                <!-- Sin imagen -->
                                <img v-if="pago.deudor.imagen == null || pago.deudor.imagen ==''" src="/src/assets/svg/User2.svg"
                                    style="width: 65px;margin-top: 0px" />
                                <!-- Con imagen -->
                                <img v-else :src="`data:image/png;base64,${pago.deudor.imagen}`" class="roundedd" style="">
                                <div>
                                    {{ pago.deudor.nombre }}
                                </div>
                            </div>
                        </div>
                        <div style="flex: 1; display: flex; flex-direction: column; margin-top: 0px;">
                            <div style="margin-top: 11px;align-self: center; display: flex; align-items: center;">
                                <div style="width: 115px; margin-bottom: 3px;">
                                    <v-card elevation="0">
                                        <v-text-field variant="outlined" density="compact"
                                            style="display:inline; padding: 0!important;" class="pa-2" type="number" min="0"
                                            :max="montoTotal" hide-details v-model="monto">
                                            <div style="margin-top: 2px;">
                                                {{ signoDinero }}
                                            </div>
                                        </v-text-field>
                                    </v-card>
                                </div>

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
                                <img v-if="pago.acreedor.imagen == null || pago.acreedor.imagen ==''" src="/src/assets/svg/User2.svg"
                                    style="width: 65px;margin-top: 0px;" />
                                <!-- Con imagen -->
                                <img v-else :src="`data:image/png;base64,${pago.acreedor.imagen}`" class="roundedd"
                                    style="">
                                <div>
                                    {{ pago.acreedor.nombre }}
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="contenedorControl">
                        <label>Nota
                            <Field v-slot="{ field }" v-model="nota" name="nota">
                                <textarea id="areatexto" v-bind="field" name="nota" />
                            </Field>
                        </label>
                    </div>

                    <Boton :loading="procesando" @click="marcarComoPagado" tipo="primario2">Marcar como pagado</Boton>
                    <!-- <Boton style="margin-left: 15px;" tipo="secundarioBorde">Pagar por PayPal</Boton> -->
                    <p style="color:red; margin-top: 15px; font-size: 14px "> {{ errorPago }}</p>
                    <p style="color:red; margin-top: 15px; font-size: 14px "> {{ errorPostSubmit }}</p>
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else>
            <ContenedorCerrable style="padding-bottom: 34px; width: 532px;" @cerrar="toggleDialog(false)">
                <template v-slot:contenido>
                    El pago fue registrado exitosamente
                    <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">
                        Cerrar</Boton>
                </template>
            </ContenedorCerrable>
        </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 65px;
    width: 65px;
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
</style>