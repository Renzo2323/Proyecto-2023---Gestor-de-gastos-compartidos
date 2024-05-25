<script>
import { RouterLink } from 'vue-router'
import Boton from '../Botones/Boton.vue';
import { Form, Field, ErrorMessage } from 'vee-validate';
import axios from 'axios';
import GestionarCategoriasGrupo from '../Modales/GestionarCategoriasGrupo.vue';

export default {
    name: 'formularioNuevoGasto',
    components: {
        Boton,
        Field,
        GestionarCategoriasGrupo
    },
    data() {
        return {
            descripcion: '',
            monto: 0,
            moneda: 'USD',
            metodoDivision: 'Partes iguales',
            metodosDivision: ['Partes iguales', 'Cantidad exacta', 'Porcentaje'],
            nota: '',
            errorPostSubmit: '',
            categoria: '',
            imagen: '',
            selectedFile: null,
            base64String: null,
            imagenPerfil: '/src/assets/images/Gasto.png',
            monedas: [],
            pagina: 1,
            participantes: [],
            participantesEnDivision: [],
            errorSeleccion: '',
            errorDivision: '',
            errorBasico: '',
            errorBasico2: '',
            pagadores: [],
            categorias: [],
            procesando: false
        }
    },
    methods: {

        async fetchMonedas() {
            const response = await axios.get(import.meta.env.VITE_API + "/api/grupos/usr/tipoMonedas",
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.tiposGrupo
                        response.data.forEach(mon => {
                            this.monedas.push(mon)
                        });
                    }
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        },
        handleFileUpload(event) {
            
            const file = event.target.files[0];
            if (file) {
                this.selectedFile = file;
                const reader = new FileReader();
                reader.onload = () => {
                    this.base64String = reader.result.split(",")[1];
                };
                reader.readAsDataURL(file);
            }
        },
        seleccionParticipantes() {
            this.errorBasico2 = ''
            
            
            //verifica
            if (this.descripcion == "") {
                this.errorBasico = "Ingrese una descripcion"
                this.errorBasico2 = ""
                return
            } else if(this.descripcion.length > 25){
                this.errorBasico = "Descripcion muy larga"
                this.errorBasico2 = ""
                return
            } else{
                this.errorBasico = ""
            }
            if (this.categoria == "") {
                this.errorBasico2 = "Ingrese una categoria"
                return
            }
            
            this.pagina = 2
        },
        seleccionDivision() {
            
            //verifica
            //Verificar que haya al menos dos personas sleccionadas, y que el monto sea mayor a 0
            if (this.pagadores.length < 1) {
                this.errorSeleccion = "Al menos una persona debe pagar"
                return
            }

            if (this.pagadores.some(p => p.pago <= 0)) {
                this.errorSeleccion = "No se puede pagar " + this.signoDinero + "0"
                return
            }

            let monto = 0
            this.pagadores.map(pagador => {
                if (pagador.pago) {
                    
                    monto += parseFloat(pagador.pago)
                    
                }
            })
            
            if (monto <= 0) {
                this.errorSeleccion = "El monto total debe ser mayor a 0"
                return
            }
            this.monto = monto
            this.recalcularCuotas()
            
            this.pagina = 3
        },
        actualizarPago(avatarId, pago) {
            
            this.integrantes.map(integrante => {
                if (integrante.id == avatarId) {
                    integrante.pago = pago
                }
            })
        },
        actualizarCuota(avatarId, cuota) {
            
            this.integrantes.map(participante => {
                if (participante.id == avatarId) {
                    participante.cuota = cuota
                }
            })
        },
        confirmar() {

            //Verifica
            //Si esta en partes iguales no verifica nada.
            //Si esta en cantidad exacta, verifica que sumen al monto
            //Si esta en porcentaje, verifica que sumen a 100
            if (this.participantesEnDivision.length < 1) {
                this.errorDivision = "Debe seleccionar al menos una persona"
                return
            }
            if (this.participantesEnDivision.some(p => p.cuota <= 0)) {
                if (this.metodoDivision == "Porcentaje") {
                    this.errorDivision = "Seleccione un porcentaje mayor a %0"
                    return
                } else {
                    this.errorDivision = "Seleccione un valor mayor a " + this.signoDinero + "0"
                    return
                }
                return
            }

            if (this.metodoDivision != "Partes iguales") {
                let cuotasSumadas = 0
                this.participantesEnDivision.map(participante => {
                    if (participante.cuota) {
                        
                        cuotasSumadas += parseFloat(participante.cuota)
                        
                    }
                })
                if (this.metodoDivision == "Cantidad exacta") {
                    if (cuotasSumadas != this.monto) {
                        this.errorDivision = "Los valores deben sumar " + this.monto
                        return
                    }
                } else if (this.metodoDivision == "Porcentaje") {
                    if (cuotasSumadas != 100) {
                        this.errorDivision = "Los porcentajes deben sumar 100"
                        return
                    }
                }
            }

            this.submitGasto()


        },
        getMonto() {
            let monto = 0
            this.pagadores.map(pagador => {
                if (pagador.pago) {
                    monto += parseFloat(pagador.pago)
                }
            })
            return monto
        },
        getCuotaTotal() {
            let cuotasSumadas = 0
            this.participantesEnDivision.map(participante => {
                if (participante.cuota) {
                    cuotasSumadas += parseFloat(participante.cuota)
                }
            })

            if (this.equal(cuotasSumadas, this.monto, 0.1)) {
                cuotasSumadas = this.monto
            }
            return cuotasSumadas
        },
        equal(x, y, tolerance = Number.EPSILON) {
            return Math.abs(x - y) < tolerance;
        },
        recalcularCuotas() {
            this.integrantes.map(integrante => {
                if (this.metodoDivision == "Partes iguales") {
                    
                    integrante.cuota = (this.monto / this.participantesEnDivision.length).toFixed(2)
                } else if (this.metodoDivision == "Cantidad exacta") {

                } else {
                }
            })
        },
        recalcularCuotasMetodo() {
            this.integrantes.map(integrante => {
                if (this.metodoDivision == "Partes iguales") {
                    
                    integrante.cuota = (this.monto / this.participantesEnDivision.length).toFixed(2)
                } else if (this.metodoDivision == "Cantidad exacta") {
                    
                    integrante.cuota = (this.monto / this.participantesEnDivision.length).toFixed(2)
                } else {
                    
                    integrante.cuota = (100 / this.participantesEnDivision.length).toFixed(2)
                }
            })
        },
        seleccionBasico() {
            this.pagina = 1
        },
        async fetchCategorias() {

            this.categorias = []

            const response = await axios.get(import.meta.env.VITE_API + "/api/categorias/usr",
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        response.data.forEach(categoria => {
                            if (categoria.estado) {
                                this.categorias.push(categoria)
                            }

                        });
                    }
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));


            const url3 = import.meta.env.VITE_API + "/api/categorias/usf/getByGrupo/" + this.$route.params.id
            const response3 = await axios.get(url3,
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
                        response.data.forEach(categoriaGrupo => {
                            if (categoriaGrupo.estado) {
                                this.categorias.push(categoriaGrupo)
                            }

                        });
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        },
        async submitGasto() {
            this.procesando = true
            let reqPagadores = []
            let reqParticipantes = []
            this.pagadores.map(pagador => {
                reqPagadores.push(
                    {
                        avatar: pagador.id,
                        pago: pagador.pago
                    }
                )
            })
            this.participantesEnDivision.map(participante => {
                reqParticipantes.push(
                    {
                        avatar: participante.id,
                        cuotaParticipacion: participante.cuota
                    }
                )
            })
            const request = {
                grupoid: this.grupoId,
                descripcion: this.descripcion,
                monto: this.monto,
                moneda: this.monedaGrupo,
                nota: this.nota,
                imagen: this.base64String,
                categoria: {
                    id: this.categoria,
                },
                metodoDivision: this.metodoDivision == "Partes iguales" ? "PARTES_IGUALES" : this.metodoDivision == "Porcentaje" ? "PORCENTAJE" : "CANTIDAD_EXACTA",
                pagadores: reqPagadores,
                participantes: reqParticipantes
            }
            

            const response = await axios.post(import.meta.env.VITE_API + "/api/gastosCompartidos/usf/nuevoGastoCompartido", request,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    
                    if (!response.data.estatus) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.$emit('GastoAgregado')
                    }
                    this.procesando = false

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                    this.procesando = false
                }.bind(this));


        },
        gestionarCategoriasGrupo() {
            
            this.$refs.gestionarCategoriasGrupo.toggleDialog(true)
        }
    },
    emits: ['cerrarDialogo', 'GastoAgregado'],
    props: {
        integrantes: Array,
        categoriasGrupo: Array,
        grupoId: Number,
        monedaGrupo: String
    },
    mounted() {
        this.fetchCategorias()
        //this.fetchMonedas()
        this.integrantes.map(participante => {
            participante.pago = 0
            participante.cuota = 0
        })
        this.participantesEnDivision = this.integrantes

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
        deshabilitado() {
            return this.metodoDivision != 'Partes iguales' ? false : true
        },

    },
    watch: {
        participantesEnDivision(viejo, nuevo) {
            this.recalcularCuotas()
        }
    }
}
</script>

<template>
    <GestionarCategoriasGrupo ref="gestionarCategoriasGrupo" @refetch="fetchCategorias" :categoriasGrupo="categoriasGrupo"
        :grupoId="$route.params.id" />
    <div v-show="pagina == 1">
        <div id="seccionControl">
            <div id="contenedorControl">
                <label>Descripcion<Field class="textField" type="text" name="descripcion" v-model="descripcion"></Field>
                </label>
                <p style="margin-top:18px" id="errorSeleccion"> {{ errorBasico }}</p>
            </div>
            <!-- <div id="contenedorControl">
                <label>Moneda
                    <Field name="moneda" as="select" id="select" v-model="moneda">
                        <option v-for="(mon, i) in monedas" :key="i" :value="mon">{{ mon }}</option>
                    </Field>
                </label>
            </div> -->
            <div id="contenedorControl">
                <div style="margin-bottom: 10px"><b>Categoría</b></div>
                <div style="display: flex; gap: 11px">
                    <v-select density="compact" v-model="categoria" hide-details :items="categorias" variant="outlined"
                        item-title="nombre" item-value="id" placeholder="Ingrese una categoria">
                        <template v-slot:item="{ props, item }">
                            <v-list-item v-bind="props" title="">
                                <template v-slot:prepend>
                                    <v-icon style="margin-right: 11px;">mdi-{{ item.raw.icono }}</v-icon>{{ item.title }}
                                </template>
                            </v-list-item>
                        </template>
                    </v-select>
                    <div style="display:flex; align-items: center;">
                        <div
                            style="background-color: var(--color-primario); padding: 0px; border-radius: 50%; width: 36px; height: 36px;">
                            <v-icon style="width: 100%;height: 100%; cursor:pointer" color="white" id="actionIcon"
                                icon="mdi-plus" @click="gestionarCategoriasGrupo" />
                        </div>
                    </div>
                </div>

                <p style="margin-top:18px" id="errorSeleccion"> {{ errorBasico2 }}</p>
            </div>
            <div style="margin-bottom: 10px"><b>Imagen</b></div>
            <div style="margin-right: 15px;position: relative;">
                <v-hover v-slot="{ isHovering, props }">
                    <div style=" border-radius: 50%; height: 140px;"
                        :style="imagenPerfil != '/src/assets/images/Gasto.png' ? 'background-color: var(--color-primario);' : ''">
                        <!-- Sin imagen -->
                        <img class="squared" v-bind="props"
                            v-if="imagenPerfil == '/src/assets/images/Gasto.png' && base64String == null"
                            src="/src/assets/images/Gasto.png" style="width: 140px;margin-top: 0px;"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.3;' : ''" />
                        <!-- Con imagen -->
                        <img v-bind="props" v-else :src="`data:image/png;base64,${base64String}`" class="squared"
                            :style="isHovering ? '-webkit-filter: grayscale(100%); filter: grayscale(100%); opacity: 0.5;' : ''">
                        <label for="file-input">
                            <div class="centeredd" v-show="isHovering" v-bind="props"
                                style="padding: 45px; border-radius: 50%; cursor: pointer;">
                                <v-icon style="font-size: 50px; color: white;"
                                    :style="imagenPerfil != '/src/assets/images/Gasto.png' ? 'color: white;' : 'color: var(--color-primario);'">mdi-image-plus</v-icon>
                            </div>
                        </label>


                        <input ref="imageInput" id="file-input" type="file" accept="image/png, image/jpeg"
                            @change="handleFileUpload" style="display: none;">
                    </div>
                </v-hover>
            </div>

            <div id="contenedorControl">
                <label>Nota
                    <Field v-slot="{ field }" v-model="nota" name="nota">
                        <textarea id="areatexto" v-bind="field" name="nota" />
                    </Field>
                </label>
            </div>

        </div>
        <div style="display:flex; flex-direction: row-reverse;">
            <Boton tipo="primario2" type="button" @click="seleccionParticipantes">Siguiente</Boton>
        </div>
    </div>




    <!-- SEGUNDA PAGINA ============================================================================== -->
    <div v-show="pagina == 2">
        <div style="margin-top: 7px;">
            Quien pagó, y cuanto?
        </div>

        <v-sheet class="overflow-y-auto" max-height="350" style="margin-top: 14px">
            <div style="display:flex; flex-direction: column; gap: 8px;">


                <template v-for="(integrante, i) in integrantes" :key="i">
                    <div style="display: flex;align-items: center; justify-content: space-between;">
                        <div style="display: flex; align-items: center;">
                            <!-- Sin imagen -->
                            <img v-if="integrante.imagen == null || integrante.imagen == ''" src="/src/assets/svg/User2.svg"
                                style="width: 40px;margin-top: 0px" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${integrante.imagen}`" class="roundedd" style="">
                            <div style="margin-left: 8px;">
                                {{ integrante.nombre }}
                            </div>
                        </div>
                        <div style="display:flex; align-items: center;">
                            <Transition>
                                <div v-if="pagadores.some(p => p.id == integrante.id)" style="width:120px;">
                                    <v-card elevation="0">
                                        <v-text-field variant="outlined" density="compact"
                                            style="display:inline; padding: 0!important;" class="pa-2" type="number" min="0"
                                            hide-details :model-value="integrante.pago"
                                            @update:model-value="actualizarPago(integrante.id, $event)">
                                            <div style="margin-top: 2px;">
                                                {{ signoDinero }}
                                            </div>

                                        </v-text-field>
                                    </v-card>
                                </div>
                            </Transition>
                            <v-checkbox v-model="pagadores" :value="integrante" hide-details
                                style="color: var(--color-primario); font-size: 23px; margin-left: 10px;"></v-checkbox>
                        </div>
                    </div>


                </template>

            </div>
        </v-sheet>

        <!-- <p>Pagadores: </p>
            <p v-for="(e, i) in pagadores" :key="i"> {{ e.nombre }} paga {{ e.pago }}</p> -->
        <div style="margin-top: 15px">
            <p style="">Total: {{ signoDinero }}{{ getMonto() }}</p>
            <p style="margin-top: 10px;" id="errorSeleccion"> {{ errorSeleccion }}</p>
        </div>

        <div style="display:flex;  margin-top: 18px; justify-content: space-between;">
            <Boton tipo="secundarioBorde" type="button" @click="seleccionBasico">Volver</Boton>
            <Boton tipo="primario2" type="button" @click="seleccionDivision">Siguiente</Boton>
        </div>
    </div>






    <!-- TERCERA PAGINA ============================================================================== -->
    <div v-show="pagina == 3">
        <div style="margin-top: 7px;">
            Entre quienes se reparte el gasto? Y como se dividen?
        </div>
        <div id="contenedorControl">
            <label>Metodo de division
                <Field style="width: 100%" name="metodoDivision" as="select" id="select" v-model="metodoDivision"
                    @update:model-value="recalcularCuotasMetodo()">
                    <option v-for="(metodo, i) in metodosDivision" :key="i" :value="metodo">{{ metodo }}</option>
                </Field>
            </label>
        </div>
        <v-sheet class="overflow-y-auto" max-height="350" style="margin-top: 14px">
            <div style="display:flex; flex-direction: column; gap: 8px;">
                <template v-for="(participante, i) in integrantes" :key="i">
                    <div style="display: flex;align-items: center; justify-content: space-between;">
                        <div style="display: flex; align-items: center;">
                            <!-- Sin imagen -->
                            <img v-if="participante.imagen == null || participante.imagen == ''" src="/src/assets/svg/User2.svg"
                                style="width: 40px;margin-top: 0px" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${participante.imagen}`" class="roundedd" style="">
                            <div style="margin-left: 8px;">
                                {{ participante.nombre }}

                            </div>
                        </div>
                        <div style="display:flex; align-items: center;">
                            <Transition>
                                <div v-if="participantesEnDivision.some(pd => pd.id == participante.id)"
                                    style="width:120px;">
                                    <v-card elevation="0" v-if="metodoDivision != 'Partes iguales'">
                                        <v-text-field variant="outlined" density="compact" :readonly=deshabilitado
                                            style="display:inline; padding: 0!important;" class="pa-2" type="number" min="0"
                                            hide-details :model-value="participante.cuota"
                                            @update:model-value="actualizarCuota(participante.id, $event)" >
                                            <div style="margin-top: 2px;">{{ metodoDivision == "Porcentaje" ? "%" :
                                                metodoDivision == "Cantidad exacta" ? signoDinero : signoDinero }}</div>
                                        </v-text-field>
                                    </v-card>
                                    <div v-else style="margin-left: 16px; padding-bottom: 3px;">
                                        {{signoDinero + participante.cuota }}
                                    </div>
                                </div>
                            </Transition>
                            <v-checkbox v-model="participantesEnDivision" :value="participante" hide-details
                                style="color: var(--color-primario); font-size: 23px; margin-left: 10px;"></v-checkbox>
                        </div>
                    </div>

                </template>

            </div>
        </v-sheet>
        <div style="margin-top: 15px">
            <p v-if="metodoDivision == 'Porcentaje'" style="margin-top: 5px; font-size: 13px">{{ getCuotaTotal() }}%/100% {{
                getCuotaTotal() > 100 ? "Demasiado!" : getCuotaTotal() == 100 ? " " : "Aun falta!" }}</p>
            <p v-else style="margin-top: 5px; font-size: 13px">{{ signoDinero }}{{ getCuotaTotal() }}/{{ signoDinero }}{{
                getMonto() }} {{ getCuotaTotal() > monto ? "Demasiado!" : getCuotaTotal() == monto ? " " : "Aun falta!" }}
            </p>
            <v-progress-linear :model-value="getCuotaTotal()" :max="metodoDivision == 'Porcentaje' ? 100 : monto"
                style="color: var(--color-primario)"></v-progress-linear>
            <p style="margin-top: 10px;" id="errorSeleccion"> {{ errorDivision }}</p>
        </div>

        <!-- <p>Pagadores: </p>
        <p v-for="(e, i) in pagadores" :key="i"> {{ e.nombre }} paga {{ e.pago }}</p>
        <br>
        <p>Participantes: </p>
        <p v-for="(e, i) in participantesEnDivision" :key="i"> {{ e.nombre }} cuota {{ e.cuota }}</p> -->
        <div style="display:flex; margin-top: 18px; justify-content: space-between;">
            <Boton tipo="secundarioBorde" type="button" @click="seleccionParticipantes">Volver</Boton>
            <Boton :loading="procesando" tipo="primario2" type="button" @click="confirmar">Confirmar</Boton>
        </div>
        <p id="errorSeleccion">{{ errorPostSubmit }}</p>
    </div>
</template>


<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
.centeredd {
    position: absolute;
    top: 50%;
    left: 15%;
    transform: translate(-50%, -50%);
}

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
    height: 40px;
    width: 40px;
    outline: 1px solid rgb(111, 111, 111);

}

#errorSeleccion {
    color: red
}
</style>
