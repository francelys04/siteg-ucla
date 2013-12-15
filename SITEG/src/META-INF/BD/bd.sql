--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.10
-- Dumped by pg_dump version 9.1.10
-- Started on 2013-12-14 20:47:18 VET

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 205 (class 3079 OID 11720)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 205
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 161 (class 1259 OID 16534)
-- Dependencies: 6
-- Name: actividad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE actividad (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.actividad OWNER TO postgres;

--
-- TOC entry 162 (class 1259 OID 16540)
-- Dependencies: 6
-- Name: actividad_requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE actividad_requisito (
    actividad_id bigint NOT NULL,
    requisito_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.actividad_requisito OWNER TO postgres;

--
-- TOC entry 163 (class 1259 OID 16543)
-- Dependencies: 6
-- Name: arbol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE arbol (
    id bigint NOT NULL,
    hijo bigint,
    nombre character varying(255),
    url character varying(255)
);


ALTER TABLE public.arbol OWNER TO postgres;

--
-- TOC entry 164 (class 1259 OID 16549)
-- Dependencies: 6
-- Name: arbol_grupo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE arbol_grupo (
    grupo_id bigint NOT NULL,
    arbol_id bigint NOT NULL
);


ALTER TABLE public.arbol_grupo OWNER TO postgres;

--
-- TOC entry 165 (class 1259 OID 16552)
-- Dependencies: 6
-- Name: area_investigacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE area_investigacion (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.area_investigacion OWNER TO postgres;

--
-- TOC entry 166 (class 1259 OID 16558)
-- Dependencies: 6
-- Name: area_programa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE area_programa (
    programa_id bigint NOT NULL,
    area_id bigint NOT NULL
);


ALTER TABLE public.area_programa OWNER TO postgres;

--
-- TOC entry 167 (class 1259 OID 16561)
-- Dependencies: 6
-- Name: avance; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE avance (
    id bigint NOT NULL,
    estatus boolean,
    fecha timestamp without time zone,
    observacion character varying(255),
    teg_id bigint
);


ALTER TABLE public.avance OWNER TO postgres;

--
-- TOC entry 168 (class 1259 OID 16564)
-- Dependencies: 6
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE categoria (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 169 (class 1259 OID 16570)
-- Dependencies: 6
-- Name: comision; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE comision (
    teg_id bigint NOT NULL,
    profesor_cedula character varying(255) NOT NULL
);


ALTER TABLE public.comision OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 16573)
-- Dependencies: 6
-- Name: condicion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE condicion (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.condicion OWNER TO postgres;

--
-- TOC entry 171 (class 1259 OID 16579)
-- Dependencies: 6
-- Name: condicion_programa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE condicion_programa (
    valor integer,
    condicion_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.condicion_programa OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16582)
-- Dependencies: 6
-- Name: cronograma; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cronograma (
    estatus boolean,
    fecha_fin timestamp without time zone,
    fecha_inicio timestamp without time zone,
    lapso_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    actividad_id bigint
);


ALTER TABLE public.cronograma OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16585)
-- Dependencies: 6
-- Name: defensa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE defensa (
    id bigint NOT NULL,
    estatus boolean,
    fecha timestamp without time zone,
    hora timestamp without time zone,
    lugar character varying(255),
    nota integer,
    teg_id bigint,
    profesor_cedula character varying(255)
);


ALTER TABLE public.defensa OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 16591)
-- Dependencies: 6
-- Name: estudiante; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE estudiante (
    cedula character varying(255) NOT NULL,
    apellido character varying(255),
    correo_electronico character varying(255),
    direcion character varying(255),
    estatus boolean,
    nombre character varying(255),
    sexo character varying(255),
    telefono_fijo character varying(255),
    telefono_movil character varying(255),
    programa_id bigint
);


ALTER TABLE public.estudiante OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 16597)
-- Dependencies: 6
-- Name: estudiante_teg; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE estudiante_teg (
    teg_id bigint NOT NULL,
    estudiante_cedula character varying(255) NOT NULL
);


ALTER TABLE public.estudiante_teg OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 16600)
-- Dependencies: 6
-- Name: etapa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE etapa (
    id bigint NOT NULL,
    descripcion character varying(255),
    duracion timestamp without time zone,
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.etapa OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 16606)
-- Dependencies: 6
-- Name: evaluacion_defensa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE evaluacion_defensa (
    id bigint NOT NULL,
    nota_individual character varying(255),
    observaciones character varying(255),
    defensa_id bigint,
    profesor_cedula character varying(255)
);


ALTER TABLE public.evaluacion_defensa OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 16612)
-- Dependencies: 6
-- Name: evaluacion_factibilidad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE evaluacion_factibilidad (
    id bigint NOT NULL,
    estatus boolean,
    fecha_evaluacion timestamp without time zone,
    observacion character varying(255)
);


ALTER TABLE public.evaluacion_factibilidad OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 16615)
-- Dependencies: 6
-- Name: factibilidad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE factibilidad (
    id bigint NOT NULL,
    estatus boolean,
    fecha timestamp without time zone,
    observacion character varying(255),
    profesor_cedula character varying(255),
    teg_id bigint
);


ALTER TABLE public.factibilidad OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 16621)
-- Dependencies: 6
-- Name: grupo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE grupo (
    id bigint NOT NULL,
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.grupo OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 16624)
-- Dependencies: 6
-- Name: grupo_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE grupo_usuario (
    usuario_id bigint NOT NULL,
    grupo_id bigint NOT NULL
);


ALTER TABLE public.grupo_usuario OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 16627)
-- Dependencies: 6
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 16629)
-- Dependencies: 6
-- Name: item_defensa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_defensa (
    ponderacion integer,
    evaluacion_defensa_id bigint NOT NULL,
    item_id bigint NOT NULL,
    defensa_id bigint NOT NULL
);


ALTER TABLE public.item_defensa OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 16632)
-- Dependencies: 6
-- Name: item_evaluacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_evaluacion (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255),
    valor integer,
    tipo character varying(255)
);


ALTER TABLE public.item_evaluacion OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16638)
-- Dependencies: 6
-- Name: item_programa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_programa (
    programa_id bigint NOT NULL,
    item_id bigint NOT NULL
);


ALTER TABLE public.item_programa OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 16641)
-- Dependencies: 6
-- Name: jurado; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE jurado (
    ponderacion integer,
    teg_id bigint NOT NULL,
    factibilidad_id bigint NOT NULL,
    item_evaluacion_id bigint NOT NULL,
    profesor_cedula character varying(255) NOT NULL,
    tipo_jurado_id bigint
);


ALTER TABLE public.jurado OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16644)
-- Dependencies: 6
-- Name: lapso; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE lapso (
    id bigint NOT NULL,
    estatus boolean,
    fecha_final timestamp without time zone,
    fecha_inicial timestamp without time zone,
    nombre character varying(255)
);


ALTER TABLE public.lapso OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16647)
-- Dependencies: 6
-- Name: profesor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE profesor (
    cedula character varying(255) NOT NULL,
    apellido character varying(255),
    correo_electronico character varying(255),
    direccion character varying(255),
    estatus boolean,
    nombre character varying(255),
    sexo character varying(255),
    telefono_fijo character varying(255),
    telefono_movil character varying(255),
    categoria_id bigint
);


ALTER TABLE public.profesor OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 16653)
-- Dependencies: 6
-- Name: profesor_area; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE profesor_area (
    profesor_cedula character varying(255) NOT NULL,
    area_id bigint NOT NULL
);


ALTER TABLE public.profesor_area OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16656)
-- Dependencies: 6
-- Name: programa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.programa OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 16662)
-- Dependencies: 6
-- Name: programa_area; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_area (
    area_investigacion_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_area OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 16665)
-- Dependencies: 6
-- Name: programa_item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_item (
    item_evaluacion_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_item OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 16668)
-- Dependencies: 6
-- Name: programa_requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_requisito (
    requisito_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_requisito OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16671)
-- Dependencies: 6
-- Name: requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE requisito (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.requisito OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 16677)
-- Dependencies: 6
-- Name: requisito_actividad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE requisito_actividad (
    actividad_id bigint NOT NULL,
    requisito_id bigint NOT NULL
);


ALTER TABLE public.requisito_actividad OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16680)
-- Dependencies: 6
-- Name: requisito_programa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE requisito_programa (
    programa_id bigint NOT NULL,
    requisito_id bigint NOT NULL
);


ALTER TABLE public.requisito_programa OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16683)
-- Dependencies: 6
-- Name: solicitud_tutoria; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitud_tutoria (
    id bigint NOT NULL,
    descripcion character varying(255),
    estado boolean,
    fecha timestamp without time zone,
    profesor_cedula character varying(255),
    tematica_id bigint
);


ALTER TABLE public.solicitud_tutoria OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16689)
-- Dependencies: 6
-- Name: solicitud_tutoria_estudiante; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitud_tutoria_estudiante (
    solicitud_id bigint NOT NULL,
    estudiante_cedula character varying(255) NOT NULL
);


ALTER TABLE public.solicitud_tutoria_estudiante OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16692)
-- Dependencies: 6
-- Name: teg; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE teg (
    id bigint NOT NULL,
    descripcion character varying(255),
    duracion timestamp without time zone,
    estatus boolean,
    fecha_entrega timestamp without time zone,
    fecha_inicio timestamp without time zone,
    titulo character varying(255),
    tematica_id bigint,
    tutor_cedula character varying(255)
);


ALTER TABLE public.teg OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16698)
-- Dependencies: 6
-- Name: teg_etapa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE teg_etapa (
    fecha timestamp without time zone,
    teg_id bigint NOT NULL,
    etapa_id bigint NOT NULL
);


ALTER TABLE public.teg_etapa OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 16701)
-- Dependencies: 6
-- Name: teg_requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE teg_requisito (
    estado character varying(255),
    fecha_entrega timestamp without time zone,
    teg_id bigint NOT NULL,
    requisito_id bigint NOT NULL
);


ALTER TABLE public.teg_requisito OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16704)
-- Dependencies: 6
-- Name: tematica; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tematica (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255),
    area_investigacion_id bigint
);


ALTER TABLE public.tematica OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16710)
-- Dependencies: 6
-- Name: tipo_jurado; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_jurado (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.tipo_jurado OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16716)
-- Dependencies: 6
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    estatus boolean,
    nombre character varying(255),
    password character varying(255)
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 2265 (class 0 OID 16534)
-- Dependencies: 161 2309
-- Data for Name: actividad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY actividad (id, descripcion, estatus, nombre) FROM stdin;
1	plant	t	planteamiento
\.


--
-- TOC entry 2266 (class 0 OID 16540)
-- Dependencies: 162 2309
-- Data for Name: actividad_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY actividad_requisito (actividad_id, requisito_id, lapso_id) FROM stdin;
\.


--
-- TOC entry 2267 (class 0 OID 16543)
-- Dependencies: 163 2309
-- Data for Name: arbol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY arbol (id, hijo, nombre, url) FROM stdin;
33	0	Trabajo Especial de Grado	arbol
37	33	Solicitar Defensa	\N
40	0	Portal Web	arbol
1	0	Archivos	arbol
2	1	Datos Basicos	arbol
17	1	Configuraciones	arbol
44	0	Seguridad	arbol
3	2	Programas	maestros/VPrograma
4	2	Areas de Investigacion	maestros/VAreaInvestigacion
5	2	Tematicas	maestros/VTematica
6	2	Requisitos	maestros/VRequisito
11	9	Profesores Por Lotes	maestros/VCargarProfesor
7	2	Actividades	maestros/VActividad
14	12	Estudiantes Por Lotes	maestros/VCargarEstudiante
15	2	Tipos de Jurado	maestros/VTipoJurado
19	17	Configurar Programas	maestros/VConfigurarPrograma
21	17	Configurar Condiciones	maestros/VCondicionPrograma
20	17	Configurar Actividades	maestros/VRequisitoActividad
18	17	Configurar Profesores	maestros/VProfesorArea
25	24	Atender Solicitudes de Tutor	transacciones/VEvaluarTutorias
26	24	Registrar Proyecto	transacciones/VRegistrarProyecto
27	24	Atender Solicitudes de Proyecto	transacciones/VVerificarSolicitudProyecto
28	24	Asignar Comision Evaluadora	transacciones/VAsignarComision
29	24	Evaluar Factibilidad	transacciones/VEvaluarFactibilidad
30	24	Registrar Factibilidad	transacciones/VRegistrarFactibilidad
31	24	Registrar Avances	transacciones/VRegistrarAvances
32	24	Evaluar Avances	transacciones/VEvaluarAvances
34	33	Registrar Trabajo	transacciones/VRegistrarTrabajo
35	33	Registrar Revisiones	transacciones/VRegistrarRevisiones
36	33	Evaluar Revisiones	transacciones/VEvaluarRevisiones
38	33	Atender Solicitudes de Defensa	transacciones/VAtenderDefensa
39	33	Calificar Defensa	transacciones/VCalificarDefensa
41	40	Crear Noticias	maestros/VCrearNoticia
8	2	Items de Evaluacion	maestros/VItem
42	40	Crear Enlaces	maestros/VCrearEnlace
43	40	Descargas	maestros/VDescarga
9	2	Profesores	arbol
45	44	Crear Usuario	maestros/VCrearUsuario
46	44	Crear Grupo	maestros/VCrearGrupo
10	9	Profesores Individual	maestros/VProfesor
12	2	Estudiantes	arbol
13	12	Estudiantes Individual	maestros/VEstudiante
16	2	Lapsos Academicos	maestros/VLapsoAcademico
22	17	Configurar Cronograma	maestros/VCrearCronograma
24	0	Proyecto	arbol
\.


--
-- TOC entry 2268 (class 0 OID 16549)
-- Dependencies: 164 2309
-- Data for Name: arbol_grupo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY arbol_grupo (grupo_id, arbol_id) FROM stdin;
101	1
101	2
101	3
101	4
101	5
101	6
101	7
101	8
101	9
101	10
101	11
101	12
101	13
101	14
101	15
101	16
101	17
101	18
101	19
101	20
101	21
101	22
101	44
101	45
101	46
102	44
102	45
102	46
102	24
102	25
102	26
102	27
102	28
102	29
102	30
102	31
102	32
102	33
102	34
102	35
102	36
102	37
102	38
102	39
102	40
102	41
102	42
102	43
\.


--
-- TOC entry 2269 (class 0 OID 16552)
-- Dependencies: 165 2309
-- Data for Name: area_investigacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY area_investigacion (id, descripcion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2270 (class 0 OID 16558)
-- Dependencies: 166 2309
-- Data for Name: area_programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY area_programa (programa_id, area_id) FROM stdin;
\.


--
-- TOC entry 2271 (class 0 OID 16561)
-- Dependencies: 167 2309
-- Data for Name: avance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY avance (id, estatus, fecha, observacion, teg_id) FROM stdin;
\.


--
-- TOC entry 2272 (class 0 OID 16564)
-- Dependencies: 168 2309
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY categoria (id, descripcion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2273 (class 0 OID 16570)
-- Dependencies: 169 2309
-- Data for Name: comision; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY comision (teg_id, profesor_cedula) FROM stdin;
\.


--
-- TOC entry 2274 (class 0 OID 16573)
-- Dependencies: 170 2309
-- Data for Name: condicion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY condicion (id, descripcion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2275 (class 0 OID 16579)
-- Dependencies: 171 2309
-- Data for Name: condicion_programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY condicion_programa (valor, condicion_id, programa_id, lapso_id) FROM stdin;
\.


--
-- TOC entry 2276 (class 0 OID 16582)
-- Dependencies: 172 2309
-- Data for Name: cronograma; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cronograma (estatus, fecha_fin, fecha_inicio, lapso_id, programa_id, actividad_id) FROM stdin;
\.


--
-- TOC entry 2277 (class 0 OID 16585)
-- Dependencies: 173 2309
-- Data for Name: defensa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY defensa (id, estatus, fecha, hora, lugar, nota, teg_id, profesor_cedula) FROM stdin;
\.


--
-- TOC entry 2278 (class 0 OID 16591)
-- Dependencies: 174 2309
-- Data for Name: estudiante; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY estudiante (cedula, apellido, correo_electronico, direcion, estatus, nombre, sexo, telefono_fijo, telefono_movil, programa_id) FROM stdin;
\.


--
-- TOC entry 2279 (class 0 OID 16597)
-- Dependencies: 175 2309
-- Data for Name: estudiante_teg; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY estudiante_teg (teg_id, estudiante_cedula) FROM stdin;
\.


--
-- TOC entry 2280 (class 0 OID 16600)
-- Dependencies: 176 2309
-- Data for Name: etapa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY etapa (id, descripcion, duracion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2281 (class 0 OID 16606)
-- Dependencies: 177 2309
-- Data for Name: evaluacion_defensa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY evaluacion_defensa (id, nota_individual, observaciones, defensa_id, profesor_cedula) FROM stdin;
\.


--
-- TOC entry 2282 (class 0 OID 16612)
-- Dependencies: 178 2309
-- Data for Name: evaluacion_factibilidad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY evaluacion_factibilidad (id, estatus, fecha_evaluacion, observacion) FROM stdin;
\.


--
-- TOC entry 2283 (class 0 OID 16615)
-- Dependencies: 179 2309
-- Data for Name: factibilidad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY factibilidad (id, estatus, fecha, observacion, profesor_cedula, teg_id) FROM stdin;
\.


--
-- TOC entry 2284 (class 0 OID 16621)
-- Dependencies: 180 2309
-- Data for Name: grupo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY grupo (id, estatus, nombre) FROM stdin;
101	t	ROLE_USER
102	t	ROLE_ADMIN
\.


--
-- TOC entry 2285 (class 0 OID 16624)
-- Dependencies: 181 2309
-- Data for Name: grupo_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY grupo_usuario (usuario_id, grupo_id) FROM stdin;
100	101
112	102
\.


--
-- TOC entry 2317 (class 0 OID 0)
-- Dependencies: 182
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 3, true);


--
-- TOC entry 2287 (class 0 OID 16629)
-- Dependencies: 183 2309
-- Data for Name: item_defensa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY item_defensa (ponderacion, evaluacion_defensa_id, item_id, defensa_id) FROM stdin;
\.


--
-- TOC entry 2288 (class 0 OID 16632)
-- Dependencies: 184 2309
-- Data for Name: item_evaluacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY item_evaluacion (id, descripcion, estatus, nombre, valor, tipo) FROM stdin;
\.


--
-- TOC entry 2289 (class 0 OID 16638)
-- Dependencies: 185 2309
-- Data for Name: item_programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY item_programa (programa_id, item_id) FROM stdin;
\.


--
-- TOC entry 2290 (class 0 OID 16641)
-- Dependencies: 186 2309
-- Data for Name: jurado; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY jurado (ponderacion, teg_id, factibilidad_id, item_evaluacion_id, profesor_cedula, tipo_jurado_id) FROM stdin;
\.


--
-- TOC entry 2291 (class 0 OID 16644)
-- Dependencies: 187 2309
-- Data for Name: lapso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY lapso (id, estatus, fecha_final, fecha_inicial, nombre) FROM stdin;
\.


--
-- TOC entry 2292 (class 0 OID 16647)
-- Dependencies: 188 2309
-- Data for Name: profesor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY profesor (cedula, apellido, correo_electronico, direccion, estatus, nombre, sexo, telefono_fijo, telefono_movil, categoria_id) FROM stdin;
\.


--
-- TOC entry 2293 (class 0 OID 16653)
-- Dependencies: 189 2309
-- Data for Name: profesor_area; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY profesor_area (profesor_cedula, area_id) FROM stdin;
\.


--
-- TOC entry 2294 (class 0 OID 16656)
-- Dependencies: 190 2309
-- Data for Name: programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY programa (id, descripcion, estatus, nombre) FROM stdin;
2	aaaa	t	matematica
3	aaaaa	t	aa
\.


--
-- TOC entry 2295 (class 0 OID 16662)
-- Dependencies: 191 2309
-- Data for Name: programa_area; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY programa_area (area_investigacion_id, programa_id, lapso_id) FROM stdin;
\.


--
-- TOC entry 2296 (class 0 OID 16665)
-- Dependencies: 192 2309
-- Data for Name: programa_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY programa_item (item_evaluacion_id, programa_id, lapso_id) FROM stdin;
\.


--
-- TOC entry 2297 (class 0 OID 16668)
-- Dependencies: 193 2309
-- Data for Name: programa_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY programa_requisito (requisito_id, programa_id, lapso_id) FROM stdin;
\.


--
-- TOC entry 2298 (class 0 OID 16671)
-- Dependencies: 194 2309
-- Data for Name: requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY requisito (id, descripcion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2299 (class 0 OID 16677)
-- Dependencies: 195 2309
-- Data for Name: requisito_actividad; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY requisito_actividad (actividad_id, requisito_id) FROM stdin;
\.


--
-- TOC entry 2300 (class 0 OID 16680)
-- Dependencies: 196 2309
-- Data for Name: requisito_programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY requisito_programa (programa_id, requisito_id) FROM stdin;
\.


--
-- TOC entry 2301 (class 0 OID 16683)
-- Dependencies: 197 2309
-- Data for Name: solicitud_tutoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY solicitud_tutoria (id, descripcion, estado, fecha, profesor_cedula, tematica_id) FROM stdin;
\.


--
-- TOC entry 2302 (class 0 OID 16689)
-- Dependencies: 198 2309
-- Data for Name: solicitud_tutoria_estudiante; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY solicitud_tutoria_estudiante (solicitud_id, estudiante_cedula) FROM stdin;
\.


--
-- TOC entry 2303 (class 0 OID 16692)
-- Dependencies: 199 2309
-- Data for Name: teg; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY teg (id, descripcion, duracion, estatus, fecha_entrega, fecha_inicio, titulo, tematica_id, tutor_cedula) FROM stdin;
\.


--
-- TOC entry 2304 (class 0 OID 16698)
-- Dependencies: 200 2309
-- Data for Name: teg_etapa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY teg_etapa (fecha, teg_id, etapa_id) FROM stdin;
\.


--
-- TOC entry 2305 (class 0 OID 16701)
-- Dependencies: 201 2309
-- Data for Name: teg_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY teg_requisito (estado, fecha_entrega, teg_id, requisito_id) FROM stdin;
\.


--
-- TOC entry 2306 (class 0 OID 16704)
-- Dependencies: 202 2309
-- Data for Name: tematica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tematica (id, descripcion, estatus, nombre, area_investigacion_id) FROM stdin;
\.


--
-- TOC entry 2307 (class 0 OID 16710)
-- Dependencies: 203 2309
-- Data for Name: tipo_jurado; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY tipo_jurado (id, descripcion, estatus, nombre) FROM stdin;
\.


--
-- TOC entry 2308 (class 0 OID 16716)
-- Dependencies: 204 2309
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuario (id, estatus, nombre, password) FROM stdin;
112	t	ysol	$2a$10$2ShZ2y.Els2iizZJvufUYO7uUh6499bK6NXoOYb.CXFelvJ8MHpMS
100	t	willi	$2a$10$sxSyH2JPrTtqXVffHFQnnOrxVNriHrG8DnlYf/ZN7LNifovQ2MoIC
6	t	cindy	$2a$10$ZQ7P6OO/UexmfNCWoSxaAuiaaD4CUKG8TIgUKkt4BnXXuUvcH9Kz.
7	t	ramon	$2a$10$ySesxrhAy6Q9KKcWVOr2zu8g3RZmExT0xNVHMsRSvhqOEelZ/uFXy
\.


--
-- TOC entry 2015 (class 2606 OID 16723)
-- Dependencies: 161 161 2310
-- Name: actividad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY actividad
    ADD CONSTRAINT actividad_pkey PRIMARY KEY (id);


--
-- TOC entry 2017 (class 2606 OID 16725)
-- Dependencies: 162 162 162 2310
-- Name: actividad_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT actividad_requisito_pkey PRIMARY KEY (actividad_id, requisito_id);


--
-- TOC entry 2021 (class 2606 OID 16727)
-- Dependencies: 164 164 164 2310
-- Name: arbol_grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT arbol_grupo_pkey PRIMARY KEY (grupo_id, arbol_id);


--
-- TOC entry 2019 (class 2606 OID 16729)
-- Dependencies: 163 163 2310
-- Name: arbol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY arbol
    ADD CONSTRAINT arbol_pkey PRIMARY KEY (id);


--
-- TOC entry 2023 (class 2606 OID 16731)
-- Dependencies: 165 165 2310
-- Name: area_investigacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY area_investigacion
    ADD CONSTRAINT area_investigacion_pkey PRIMARY KEY (id);


--
-- TOC entry 2025 (class 2606 OID 16733)
-- Dependencies: 166 166 166 2310
-- Name: area_programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY area_programa
    ADD CONSTRAINT area_programa_pkey PRIMARY KEY (programa_id, area_id);


--
-- TOC entry 2027 (class 2606 OID 16735)
-- Dependencies: 167 167 2310
-- Name: avance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY avance
    ADD CONSTRAINT avance_pkey PRIMARY KEY (id);


--
-- TOC entry 2029 (class 2606 OID 16737)
-- Dependencies: 168 168 2310
-- Name: categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id);


--
-- TOC entry 2031 (class 2606 OID 16739)
-- Dependencies: 169 169 169 2310
-- Name: comision_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT comision_pkey PRIMARY KEY (teg_id, profesor_cedula);


--
-- TOC entry 2033 (class 2606 OID 16741)
-- Dependencies: 170 170 2310
-- Name: condicion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY condicion
    ADD CONSTRAINT condicion_pkey PRIMARY KEY (id);


--
-- TOC entry 2035 (class 2606 OID 16743)
-- Dependencies: 171 171 171 2310
-- Name: condicion_programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT condicion_programa_pkey PRIMARY KEY (condicion_id, programa_id);


--
-- TOC entry 2037 (class 2606 OID 16745)
-- Dependencies: 172 172 172 2310
-- Name: cronograma_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT cronograma_pkey PRIMARY KEY (lapso_id, programa_id);


--
-- TOC entry 2039 (class 2606 OID 16747)
-- Dependencies: 173 173 2310
-- Name: defensa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT defensa_pkey PRIMARY KEY (id);


--
-- TOC entry 2041 (class 2606 OID 16749)
-- Dependencies: 174 174 2310
-- Name: estudiante_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estudiante
    ADD CONSTRAINT estudiante_pkey PRIMARY KEY (cedula);


--
-- TOC entry 2043 (class 2606 OID 16751)
-- Dependencies: 175 175 175 2310
-- Name: estudiante_teg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT estudiante_teg_pkey PRIMARY KEY (teg_id, estudiante_cedula);


--
-- TOC entry 2045 (class 2606 OID 16753)
-- Dependencies: 176 176 2310
-- Name: etapa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY etapa
    ADD CONSTRAINT etapa_pkey PRIMARY KEY (id);


--
-- TOC entry 2047 (class 2606 OID 16755)
-- Dependencies: 177 177 2310
-- Name: evaluacion_defensa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY evaluacion_defensa
    ADD CONSTRAINT evaluacion_defensa_pkey PRIMARY KEY (id);


--
-- TOC entry 2049 (class 2606 OID 16757)
-- Dependencies: 178 178 2310
-- Name: evaluacion_factibilidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY evaluacion_factibilidad
    ADD CONSTRAINT evaluacion_factibilidad_pkey PRIMARY KEY (id);


--
-- TOC entry 2051 (class 2606 OID 16759)
-- Dependencies: 179 179 2310
-- Name: factibilidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT factibilidad_pkey PRIMARY KEY (id);


--
-- TOC entry 2053 (class 2606 OID 16761)
-- Dependencies: 180 180 2310
-- Name: grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT grupo_pkey PRIMARY KEY (id);


--
-- TOC entry 2055 (class 2606 OID 16763)
-- Dependencies: 181 181 181 2310
-- Name: grupo_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT grupo_usuario_pkey PRIMARY KEY (usuario_id, grupo_id);


--
-- TOC entry 2057 (class 2606 OID 16765)
-- Dependencies: 183 183 183 2310
-- Name: item_defensa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT item_defensa_pkey PRIMARY KEY (evaluacion_defensa_id, item_id);


--
-- TOC entry 2059 (class 2606 OID 16767)
-- Dependencies: 184 184 2310
-- Name: item_evaluacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_evaluacion
    ADD CONSTRAINT item_evaluacion_pkey PRIMARY KEY (id);


--
-- TOC entry 2061 (class 2606 OID 16769)
-- Dependencies: 185 185 185 2310
-- Name: item_programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_programa
    ADD CONSTRAINT item_programa_pkey PRIMARY KEY (programa_id, item_id);


--
-- TOC entry 2063 (class 2606 OID 16771)
-- Dependencies: 186 186 186 2310
-- Name: jurado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT jurado_pkey PRIMARY KEY (factibilidad_id, item_evaluacion_id);


--
-- TOC entry 2065 (class 2606 OID 16773)
-- Dependencies: 187 187 2310
-- Name: lapso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY lapso
    ADD CONSTRAINT lapso_pkey PRIMARY KEY (id);


--
-- TOC entry 2069 (class 2606 OID 16775)
-- Dependencies: 189 189 189 2310
-- Name: profesor_area_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT profesor_area_pkey PRIMARY KEY (profesor_cedula, area_id);


--
-- TOC entry 2067 (class 2606 OID 16777)
-- Dependencies: 188 188 2310
-- Name: profesor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY profesor
    ADD CONSTRAINT profesor_pkey PRIMARY KEY (cedula);


--
-- TOC entry 2073 (class 2606 OID 16779)
-- Dependencies: 191 191 191 2310
-- Name: programa_area_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT programa_area_pkey PRIMARY KEY (area_investigacion_id, programa_id);


--
-- TOC entry 2075 (class 2606 OID 16781)
-- Dependencies: 192 192 192 2310
-- Name: programa_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT programa_item_pkey PRIMARY KEY (item_evaluacion_id, programa_id);


--
-- TOC entry 2071 (class 2606 OID 16783)
-- Dependencies: 190 190 2310
-- Name: programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa
    ADD CONSTRAINT programa_pkey PRIMARY KEY (id);


--
-- TOC entry 2077 (class 2606 OID 16785)
-- Dependencies: 193 193 193 2310
-- Name: programa_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT programa_requisito_pkey PRIMARY KEY (programa_id, requisito_id);


--
-- TOC entry 2081 (class 2606 OID 16787)
-- Dependencies: 195 195 195 2310
-- Name: requisito_actividad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY requisito_actividad
    ADD CONSTRAINT requisito_actividad_pkey PRIMARY KEY (actividad_id, requisito_id);


--
-- TOC entry 2079 (class 2606 OID 16789)
-- Dependencies: 194 194 2310
-- Name: requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY requisito
    ADD CONSTRAINT requisito_pkey PRIMARY KEY (id);


--
-- TOC entry 2083 (class 2606 OID 16791)
-- Dependencies: 196 196 196 2310
-- Name: requisito_programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY requisito_programa
    ADD CONSTRAINT requisito_programa_pkey PRIMARY KEY (programa_id, requisito_id);


--
-- TOC entry 2087 (class 2606 OID 16793)
-- Dependencies: 198 198 198 2310
-- Name: solicitud_tutoria_estudiante_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT solicitud_tutoria_estudiante_pkey PRIMARY KEY (solicitud_id, estudiante_cedula);


--
-- TOC entry 2085 (class 2606 OID 16795)
-- Dependencies: 197 197 2310
-- Name: solicitud_tutoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT solicitud_tutoria_pkey PRIMARY KEY (id);


--
-- TOC entry 2091 (class 2606 OID 16797)
-- Dependencies: 200 200 200 2310
-- Name: teg_etapa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT teg_etapa_pkey PRIMARY KEY (etapa_id, teg_id);


--
-- TOC entry 2089 (class 2606 OID 16799)
-- Dependencies: 199 199 2310
-- Name: teg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT teg_pkey PRIMARY KEY (id);


--
-- TOC entry 2093 (class 2606 OID 16801)
-- Dependencies: 201 201 201 2310
-- Name: teg_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT teg_requisito_pkey PRIMARY KEY (requisito_id, teg_id);


--
-- TOC entry 2095 (class 2606 OID 16803)
-- Dependencies: 202 202 2310
-- Name: tematica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tematica
    ADD CONSTRAINT tematica_pkey PRIMARY KEY (id);


--
-- TOC entry 2097 (class 2606 OID 16805)
-- Dependencies: 203 203 2310
-- Name: tipo_jurado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_jurado
    ADD CONSTRAINT tipo_jurado_pkey PRIMARY KEY (id);


--
-- TOC entry 2099 (class 2606 OID 16807)
-- Dependencies: 204 204 2310
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2159 (class 2606 OID 16808)
-- Dependencies: 199 200 2088 2310
-- Name: fk1b5cb59a49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT fk1b5cb59a49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2160 (class 2606 OID 16813)
-- Dependencies: 2044 176 200 2310
-- Name: fk1b5cb59a76d87132; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT fk1b5cb59a76d87132 FOREIGN KEY (etapa_id) REFERENCES etapa(id);


--
-- TOC entry 2157 (class 2606 OID 16818)
-- Dependencies: 202 199 2094 2310
-- Name: fk1c01681f0a162; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT fk1c01681f0a162 FOREIGN KEY (tematica_id) REFERENCES tematica(id);


--
-- TOC entry 2158 (class 2606 OID 16823)
-- Dependencies: 199 188 2066 2310
-- Name: fk1c016a4f276e3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT fk1c016a4f276e3 FOREIGN KEY (tutor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2151 (class 2606 OID 16828)
-- Dependencies: 196 194 2078 2310
-- Name: fk1c892919857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requisito_programa
    ADD CONSTRAINT fk1c892919857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2152 (class 2606 OID 16833)
-- Dependencies: 190 196 2070 2310
-- Name: fk1c892919caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requisito_programa
    ADD CONSTRAINT fk1c892919caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2125 (class 2606 OID 16838)
-- Dependencies: 2098 181 204 2310
-- Name: fk1ecb2d584241c752; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT fk1ecb2d584241c752 FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2126 (class 2606 OID 16843)
-- Dependencies: 181 2052 180 2310
-- Name: fk1ecb2d58ff112f72; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT fk1ecb2d58ff112f72 FOREIGN KEY (grupo_id) REFERENCES grupo(id);


--
-- TOC entry 2100 (class 2606 OID 16848)
-- Dependencies: 187 2064 162 2310
-- Name: fk228a90817b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a90817b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2101 (class 2606 OID 16853)
-- Dependencies: 2078 194 162 2310
-- Name: fk228a9081857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a9081857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2102 (class 2606 OID 16858)
-- Dependencies: 2014 161 162 2310
-- Name: fk228a9081d101c2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a9081d101c2f2 FOREIGN KEY (actividad_id) REFERENCES actividad(id);


--
-- TOC entry 2140 (class 2606 OID 16863)
-- Dependencies: 191 187 2064 2310
-- Name: fk32e5c92f7b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92f7b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2141 (class 2606 OID 16868)
-- Dependencies: 165 2022 191 2310
-- Name: fk32e5c92fb5e71917; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92fb5e71917 FOREIGN KEY (area_investigacion_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2142 (class 2606 OID 16873)
-- Dependencies: 191 2070 190 2310
-- Name: fk32e5c92fcaebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92fcaebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2143 (class 2606 OID 16878)
-- Dependencies: 192 2064 187 2310
-- Name: fk32e973b57b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b57b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2144 (class 2606 OID 16883)
-- Dependencies: 2070 190 192 2310
-- Name: fk32e973b5caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b5caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2145 (class 2606 OID 16888)
-- Dependencies: 192 2058 184 2310
-- Name: fk32e973b5ec331a89; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b5ec331a89 FOREIGN KEY (item_evaluacion_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2121 (class 2606 OID 16893)
-- Dependencies: 188 2066 177 2310
-- Name: fk4c1844ea4638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY evaluacion_defensa
    ADD CONSTRAINT fk4c1844ea4638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2122 (class 2606 OID 16898)
-- Dependencies: 173 177 2038 2310
-- Name: fk4c1844ea8be4492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY evaluacion_defensa
    ADD CONSTRAINT fk4c1844ea8be4492 FOREIGN KEY (defensa_id) REFERENCES defensa(id);


--
-- TOC entry 2153 (class 2606 OID 16903)
-- Dependencies: 188 197 2066 2310
-- Name: fk56ac30334638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT fk56ac30334638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2154 (class 2606 OID 16908)
-- Dependencies: 197 202 2094 2310
-- Name: fk56ac303381f0a162; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT fk56ac303381f0a162 FOREIGN KEY (tematica_id) REFERENCES tematica(id);


--
-- TOC entry 2146 (class 2606 OID 16913)
-- Dependencies: 187 193 2064 2310
-- Name: fk5788eaa17b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa17b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2147 (class 2606 OID 16918)
-- Dependencies: 193 2078 194 2310
-- Name: fk5788eaa1857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa1857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2148 (class 2606 OID 16923)
-- Dependencies: 2070 193 190 2310
-- Name: fk5788eaa1caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa1caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2116 (class 2606 OID 16928)
-- Dependencies: 2066 173 188 2310
-- Name: fk5c158e3c4638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT fk5c158e3c4638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2117 (class 2606 OID 16933)
-- Dependencies: 2088 173 199 2310
-- Name: fk5c158e3c49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT fk5c158e3c49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2110 (class 2606 OID 16938)
-- Dependencies: 171 2064 187 2310
-- Name: fk5d6696707b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d6696707b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2111 (class 2606 OID 16943)
-- Dependencies: 171 170 2032 2310
-- Name: fk5d66967081fb8492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d66967081fb8492 FOREIGN KEY (condicion_id) REFERENCES condicion(id);


--
-- TOC entry 2112 (class 2606 OID 16948)
-- Dependencies: 2070 190 171 2310
-- Name: fk5d669670caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d669670caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2113 (class 2606 OID 16953)
-- Dependencies: 172 2064 187 2310
-- Name: fk7bac5e97b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e97b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2114 (class 2606 OID 16958)
-- Dependencies: 172 190 2070 2310
-- Name: fk7bac5e9caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e9caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2115 (class 2606 OID 16963)
-- Dependencies: 2014 172 161 2310
-- Name: fk7bac5e9d101c2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e9d101c2f2 FOREIGN KEY (actividad_id) REFERENCES actividad(id);


--
-- TOC entry 2138 (class 2606 OID 16968)
-- Dependencies: 188 189 2066 2310
-- Name: fk8bc6b5c24638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT fk8bc6b5c24638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2139 (class 2606 OID 16973)
-- Dependencies: 165 189 2022 2310
-- Name: fk8bc6b5c266e3cd8b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT fk8bc6b5c266e3cd8b FOREIGN KEY (area_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2155 (class 2606 OID 16978)
-- Dependencies: 197 2084 198 2310
-- Name: fk9ab055eb8fe1ec8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT fk9ab055eb8fe1ec8 FOREIGN KEY (solicitud_id) REFERENCES solicitud_tutoria(id);


--
-- TOC entry 2156 (class 2606 OID 16983)
-- Dependencies: 2040 198 174 2310
-- Name: fk9ab055ebf20a6af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT fk9ab055ebf20a6af FOREIGN KEY (estudiante_cedula) REFERENCES estudiante(cedula);


--
-- TOC entry 2127 (class 2606 OID 16988)
-- Dependencies: 183 2038 173 2310
-- Name: fka3fed108be4492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT fka3fed108be4492 FOREIGN KEY (defensa_id) REFERENCES defensa(id);


--
-- TOC entry 2128 (class 2606 OID 16993)
-- Dependencies: 183 184 2058 2310
-- Name: fka3fed10bdeb82cf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT fka3fed10bdeb82cf FOREIGN KEY (item_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2129 (class 2606 OID 16998)
-- Dependencies: 2046 177 183 2310
-- Name: fka3fed10db92af77; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT fka3fed10db92af77 FOREIGN KEY (evaluacion_defensa_id) REFERENCES evaluacion_defensa(id);


--
-- TOC entry 2107 (class 2606 OID 17003)
-- Dependencies: 199 2088 167 2310
-- Name: fkac32ab0449ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avance
    ADD CONSTRAINT fkac32ab0449ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2163 (class 2606 OID 17008)
-- Dependencies: 165 202 2022 2310
-- Name: fkb06b2ad8b5e71917; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tematica
    ADD CONSTRAINT fkb06b2ad8b5e71917 FOREIGN KEY (area_investigacion_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2123 (class 2606 OID 17013)
-- Dependencies: 2066 179 188 2310
-- Name: fkb7a6c2e64638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT fkb7a6c2e64638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2124 (class 2606 OID 17018)
-- Dependencies: 179 2088 199 2310
-- Name: fkb7a6c2e649ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT fkb7a6c2e649ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2149 (class 2606 OID 17023)
-- Dependencies: 2078 195 194 2310
-- Name: fkba8c9101857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requisito_actividad
    ADD CONSTRAINT fkba8c9101857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2150 (class 2606 OID 17028)
-- Dependencies: 195 161 2014 2310
-- Name: fkba8c9101d101c2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY requisito_actividad
    ADD CONSTRAINT fkba8c9101d101c2f2 FOREIGN KEY (actividad_id) REFERENCES actividad(id);


--
-- TOC entry 2132 (class 2606 OID 17033)
-- Dependencies: 2066 186 188 2310
-- Name: fkbb87bac54638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac54638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2133 (class 2606 OID 17038)
-- Dependencies: 199 186 2088 2310
-- Name: fkbb87bac549ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac549ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2134 (class 2606 OID 17043)
-- Dependencies: 179 186 2050 2310
-- Name: fkbb87bac579a06aa2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac579a06aa2 FOREIGN KEY (factibilidad_id) REFERENCES factibilidad(id);


--
-- TOC entry 2135 (class 2606 OID 17048)
-- Dependencies: 203 186 2096 2310
-- Name: fkbb87bac5cafdd94b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac5cafdd94b FOREIGN KEY (tipo_jurado_id) REFERENCES tipo_jurado(id);


--
-- TOC entry 2136 (class 2606 OID 17053)
-- Dependencies: 184 186 2058 2310
-- Name: fkbb87bac5ec331a89; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac5ec331a89 FOREIGN KEY (item_evaluacion_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2137 (class 2606 OID 17058)
-- Dependencies: 168 188 2028 2310
-- Name: fkc440f5eaf6339732; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor
    ADD CONSTRAINT fkc440f5eaf6339732 FOREIGN KEY (categoria_id) REFERENCES categoria(id);


--
-- TOC entry 2161 (class 2606 OID 17063)
-- Dependencies: 199 201 2088 2310
-- Name: fkd54ee51a49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT fkd54ee51a49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2162 (class 2606 OID 17068)
-- Dependencies: 194 201 2078 2310
-- Name: fkd54ee51a857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT fkd54ee51a857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2130 (class 2606 OID 17073)
-- Dependencies: 184 185 2058 2310
-- Name: fkdb762e09bdeb82cf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_programa
    ADD CONSTRAINT fkdb762e09bdeb82cf FOREIGN KEY (item_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2131 (class 2606 OID 17078)
-- Dependencies: 190 185 2070 2310
-- Name: fkdb762e09caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_programa
    ADD CONSTRAINT fkdb762e09caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2108 (class 2606 OID 17083)
-- Dependencies: 188 169 2066 2310
-- Name: fkdbe5c9bd4638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT fkdbe5c9bd4638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2109 (class 2606 OID 17088)
-- Dependencies: 169 2088 199 2310
-- Name: fkdbe5c9bd49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT fkdbe5c9bd49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2105 (class 2606 OID 17093)
-- Dependencies: 2022 166 165 2310
-- Name: fke48961cf66e3cd8b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY area_programa
    ADD CONSTRAINT fke48961cf66e3cd8b FOREIGN KEY (area_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2106 (class 2606 OID 17098)
-- Dependencies: 166 2070 190 2310
-- Name: fke48961cfcaebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY area_programa
    ADD CONSTRAINT fke48961cfcaebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2119 (class 2606 OID 17103)
-- Dependencies: 175 2088 199 2310
-- Name: fkea10a42949ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT fkea10a42949ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2120 (class 2606 OID 17108)
-- Dependencies: 2040 174 175 2310
-- Name: fkea10a429bf20a6af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT fkea10a429bf20a6af FOREIGN KEY (estudiante_cedula) REFERENCES estudiante(cedula);


--
-- TOC entry 2118 (class 2606 OID 17113)
-- Dependencies: 190 2070 174 2310
-- Name: fkeb92edd2caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante
    ADD CONSTRAINT fkeb92edd2caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2103 (class 2606 OID 17118)
-- Dependencies: 2018 164 163 2310
-- Name: fkf94f7cd86ef9c552; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT fkf94f7cd86ef9c552 FOREIGN KEY (arbol_id) REFERENCES arbol(id);


--
-- TOC entry 2104 (class 2606 OID 17123)
-- Dependencies: 180 2052 164 2310
-- Name: fkf94f7cd8ff112f72; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT fkf94f7cd8ff112f72 FOREIGN KEY (grupo_id) REFERENCES grupo(id);


--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-12-14 20:47:18 VET

--
-- PostgreSQL database dump complete
--

