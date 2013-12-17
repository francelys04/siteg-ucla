--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.0
-- Dumped by pg_dump version 9.3.0
-- Started on 2013-12-17 15:55:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 209 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2271 (class 0 OID 0)
-- Dependencies: 209
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 19805)
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
-- TOC entry 172 (class 1259 OID 19813)
-- Name: actividad_requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE actividad_requisito (
    actividad_id bigint NOT NULL,
    requisito_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.actividad_requisito OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 20419)
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
-- TOC entry 208 (class 1259 OID 20425)
-- Name: arbol_grupo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE arbol_grupo (
    grupo_id bigint NOT NULL,
    arbol_id bigint NOT NULL
);


ALTER TABLE public.arbol_grupo OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 19831)
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
-- TOC entry 174 (class 1259 OID 19839)
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
-- TOC entry 175 (class 1259 OID 19844)
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
-- TOC entry 176 (class 1259 OID 19852)
-- Name: comision; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE comision (
    teg_id bigint NOT NULL,
    profesor_cedula character varying(255) NOT NULL
);


ALTER TABLE public.comision OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 19857)
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
-- TOC entry 178 (class 1259 OID 19865)
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
-- TOC entry 179 (class 1259 OID 19870)
-- Name: cronograma; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cronograma (
    estatus boolean,
    fecha_fin timestamp without time zone,
    fecha_inicio timestamp without time zone,
    actividad_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.cronograma OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 19875)
-- Name: defensa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE defensa (
    id bigint NOT NULL,
    estatus boolean,
    fecha timestamp without time zone,
    hora timestamp without time zone,
    lugar character varying(255),
    nota integer,
    profesor_cedula character varying(255),
    teg_id bigint
);


ALTER TABLE public.defensa OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 19883)
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
-- TOC entry 182 (class 1259 OID 19891)
-- Name: estudiante_teg; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE estudiante_teg (
    teg_id bigint NOT NULL,
    estudiante_cedula character varying(255) NOT NULL
);


ALTER TABLE public.estudiante_teg OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 19896)
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
-- TOC entry 184 (class 1259 OID 19904)
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
-- TOC entry 185 (class 1259 OID 19909)
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
-- TOC entry 204 (class 1259 OID 20330)
-- Name: grupo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE grupo (
    id bigint NOT NULL,
    estatus boolean,
    nombre character varying(255)
);


ALTER TABLE public.grupo OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 20333)
-- Name: grupo_usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE grupo_usuario (
    usuario_id bigint NOT NULL,
    grupo_id bigint NOT NULL
);


ALTER TABLE public.grupo_usuario OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 20294)
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
-- TOC entry 186 (class 1259 OID 19927)
-- Name: item_defensa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_defensa (
    ponderacion character varying(255),
    defensa_id bigint NOT NULL,
    item_id bigint NOT NULL
);


ALTER TABLE public.item_defensa OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 19932)
-- Name: item_evaluacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_evaluacion (
    id bigint NOT NULL,
    descripcion character varying(255),
    estatus boolean,
    nombre character varying(255),
    tipo character varying(255)
);


ALTER TABLE public.item_evaluacion OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 19940)
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
-- TOC entry 189 (class 1259 OID 19945)
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
-- TOC entry 190 (class 1259 OID 19950)
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
-- TOC entry 191 (class 1259 OID 19958)
-- Name: profesor_area; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE profesor_area (
    profesor_cedula character varying(255) NOT NULL,
    area_id bigint NOT NULL
);


ALTER TABLE public.profesor_area OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 19963)
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
-- TOC entry 193 (class 1259 OID 19971)
-- Name: programa_area; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_area (
    area_investigacion_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_area OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 19976)
-- Name: programa_item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_item (
    item_evaluacion_id bigint NOT NULL,
    programa_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_item OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 19294)
-- Name: programa_requisito; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE programa_requisito (
    programa_id bigint NOT NULL,
    requisito_id bigint NOT NULL,
    lapso_id bigint
);


ALTER TABLE public.programa_requisito OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 19981)
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
-- TOC entry 196 (class 1259 OID 19989)
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
-- TOC entry 197 (class 1259 OID 19997)
-- Name: solicitud_tutoria_estudiante; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE solicitud_tutoria_estudiante (
    solicitud_id bigint NOT NULL,
    estudiante_cedula character varying(255) NOT NULL
);


ALTER TABLE public.solicitud_tutoria_estudiante OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 20002)
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
-- TOC entry 199 (class 1259 OID 20010)
-- Name: teg_etapa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE teg_etapa (
    fecha timestamp without time zone,
    teg_id bigint NOT NULL,
    etapa_id bigint NOT NULL
);


ALTER TABLE public.teg_etapa OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 20015)
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
-- TOC entry 201 (class 1259 OID 20020)
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
-- TOC entry 202 (class 1259 OID 20028)
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
-- TOC entry 206 (class 1259 OID 20336)
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
-- TOC entry 2226 (class 0 OID 19805)
-- Dependencies: 171
-- Data for Name: actividad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO actividad VALUES (1, 'actividad 1', true, 'actividad1');
INSERT INTO actividad VALUES (2, 'actividad 2', true, 'actividad2');
INSERT INTO actividad VALUES (3, 'actividad ', true, 'actividad3');
INSERT INTO actividad VALUES (10, 'otra actividad', true, 'actividad6');
INSERT INTO actividad VALUES (11, 'otra actividad', true, 'actividad6');
INSERT INTO actividad VALUES (12, 'otra actividad', true, 'actividad');
INSERT INTO actividad VALUES (13, 'otra actividad', true, 'actividad7');


--
-- TOC entry 2227 (class 0 OID 19813)
-- Dependencies: 172
-- Data for Name: actividad_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2262 (class 0 OID 20419)
-- Dependencies: 207
-- Data for Name: arbol; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO arbol VALUES (33, 0, 'Trabajo Especial de Grado', 'arbol');
INSERT INTO arbol VALUES (37, 33, 'Solicitar Defensa', NULL);
INSERT INTO arbol VALUES (40, 0, 'Portal Web', 'arbol');
INSERT INTO arbol VALUES (1, 0, 'Archivos', 'arbol');
INSERT INTO arbol VALUES (2, 1, 'Datos Basicos', 'arbol');
INSERT INTO arbol VALUES (17, 1, 'Configuraciones', 'arbol');
INSERT INTO arbol VALUES (44, 0, 'Seguridad', 'arbol');
INSERT INTO arbol VALUES (3, 2, 'Programas', 'maestros/VPrograma');
INSERT INTO arbol VALUES (4, 2, 'Areas de Investigacion', 'maestros/VAreaInvestigacion');
INSERT INTO arbol VALUES (5, 2, 'Tematicas', 'maestros/VTematica');
INSERT INTO arbol VALUES (6, 2, 'Requisitos', 'maestros/VRequisito');
INSERT INTO arbol VALUES (11, 9, 'Profesores Por Lotes', 'maestros/VCargarProfesor');
INSERT INTO arbol VALUES (7, 2, 'Actividades', 'maestros/VActividad');
INSERT INTO arbol VALUES (14, 12, 'Estudiantes Por Lotes', 'maestros/VCargarEstudiante');
INSERT INTO arbol VALUES (15, 2, 'Tipos de Jurado', 'maestros/VTipoJurado');
INSERT INTO arbol VALUES (19, 17, 'Configurar Programas', 'maestros/VConfigurarPrograma');
INSERT INTO arbol VALUES (21, 17, 'Configurar Condiciones', 'maestros/VCondicionPrograma');
INSERT INTO arbol VALUES (20, 17, 'Configurar Actividades', 'maestros/VRequisitoActividad');
INSERT INTO arbol VALUES (18, 17, 'Configurar Profesores', 'maestros/VProfesorArea');
INSERT INTO arbol VALUES (25, 24, 'Atender Solicitudes de Tutor', 'transacciones/VEvaluarTutorias');
INSERT INTO arbol VALUES (26, 24, 'Registrar Proyecto', 'transacciones/VRegistrarProyecto');
INSERT INTO arbol VALUES (27, 24, 'Atender Solicitudes de Proyecto', 'transacciones/VVerificarSolicitudProyecto');
INSERT INTO arbol VALUES (28, 24, 'Asignar Comision Evaluadora', 'transacciones/VAsignarComision');
INSERT INTO arbol VALUES (29, 24, 'Evaluar Factibilidad', 'transacciones/VEvaluarFactibilidad');
INSERT INTO arbol VALUES (30, 24, 'Registrar Factibilidad', 'transacciones/VRegistrarFactibilidad');
INSERT INTO arbol VALUES (31, 24, 'Registrar Avances', 'transacciones/VRegistrarAvances');
INSERT INTO arbol VALUES (32, 24, 'Evaluar Avances', 'transacciones/VEvaluarAvances');
INSERT INTO arbol VALUES (34, 33, 'Registrar Trabajo', 'transacciones/VRegistrarTrabajo');
INSERT INTO arbol VALUES (35, 33, 'Registrar Revisiones', 'transacciones/VRegistrarRevisiones');
INSERT INTO arbol VALUES (36, 33, 'Evaluar Revisiones', 'transacciones/VEvaluarRevisiones');
INSERT INTO arbol VALUES (38, 33, 'Atender Solicitudes de Defensa', 'transacciones/VAtenderDefensa');
INSERT INTO arbol VALUES (39, 33, 'Calificar Defensa', 'transacciones/VCalificarDefensa');
INSERT INTO arbol VALUES (41, 40, 'Crear Noticias', 'maestros/VCrearNoticia');
INSERT INTO arbol VALUES (8, 2, 'Items de Evaluacion', 'maestros/VItem');
INSERT INTO arbol VALUES (42, 40, 'Crear Enlaces', 'maestros/VCrearEnlace');
INSERT INTO arbol VALUES (43, 40, 'Descargas', 'maestros/VDescarga');
INSERT INTO arbol VALUES (9, 2, 'Profesores', 'arbol');
INSERT INTO arbol VALUES (45, 44, 'Crear Usuario', 'maestros/VCrearUsuario');
INSERT INTO arbol VALUES (46, 44, 'Crear Grupo', 'maestros/VCrearGrupo');
INSERT INTO arbol VALUES (10, 9, 'Profesores Individual', 'maestros/VProfesor');
INSERT INTO arbol VALUES (12, 2, 'Estudiantes', 'arbol');
INSERT INTO arbol VALUES (13, 12, 'Estudiantes Individual', 'maestros/VEstudiante');
INSERT INTO arbol VALUES (16, 2, 'Lapsos Academicos', 'maestros/VLapsoAcademico');
INSERT INTO arbol VALUES (22, 17, 'Configurar Cronograma', 'maestros/VCrearCronograma');
INSERT INTO arbol VALUES (24, 0, 'Proyecto', 'arbol');


--
-- TOC entry 2263 (class 0 OID 20425)
-- Dependencies: 208
-- Data for Name: arbol_grupo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO arbol_grupo VALUES (101, 1);
INSERT INTO arbol_grupo VALUES (101, 2);
INSERT INTO arbol_grupo VALUES (101, 3);
INSERT INTO arbol_grupo VALUES (101, 4);
INSERT INTO arbol_grupo VALUES (101, 5);
INSERT INTO arbol_grupo VALUES (101, 6);
INSERT INTO arbol_grupo VALUES (101, 7);
INSERT INTO arbol_grupo VALUES (101, 8);
INSERT INTO arbol_grupo VALUES (101, 9);
INSERT INTO arbol_grupo VALUES (101, 10);
INSERT INTO arbol_grupo VALUES (101, 11);
INSERT INTO arbol_grupo VALUES (101, 12);
INSERT INTO arbol_grupo VALUES (101, 13);
INSERT INTO arbol_grupo VALUES (101, 14);
INSERT INTO arbol_grupo VALUES (101, 15);
INSERT INTO arbol_grupo VALUES (101, 16);
INSERT INTO arbol_grupo VALUES (101, 17);
INSERT INTO arbol_grupo VALUES (101, 18);
INSERT INTO arbol_grupo VALUES (101, 19);
INSERT INTO arbol_grupo VALUES (101, 20);
INSERT INTO arbol_grupo VALUES (101, 21);
INSERT INTO arbol_grupo VALUES (101, 22);
INSERT INTO arbol_grupo VALUES (101, 44);
INSERT INTO arbol_grupo VALUES (101, 45);
INSERT INTO arbol_grupo VALUES (101, 46);
INSERT INTO arbol_grupo VALUES (102, 44);
INSERT INTO arbol_grupo VALUES (102, 45);
INSERT INTO arbol_grupo VALUES (102, 46);
INSERT INTO arbol_grupo VALUES (102, 24);
INSERT INTO arbol_grupo VALUES (102, 25);
INSERT INTO arbol_grupo VALUES (102, 26);
INSERT INTO arbol_grupo VALUES (102, 27);
INSERT INTO arbol_grupo VALUES (102, 28);
INSERT INTO arbol_grupo VALUES (102, 29);
INSERT INTO arbol_grupo VALUES (102, 30);
INSERT INTO arbol_grupo VALUES (102, 31);
INSERT INTO arbol_grupo VALUES (102, 32);
INSERT INTO arbol_grupo VALUES (102, 33);
INSERT INTO arbol_grupo VALUES (102, 34);
INSERT INTO arbol_grupo VALUES (102, 35);
INSERT INTO arbol_grupo VALUES (102, 36);
INSERT INTO arbol_grupo VALUES (102, 37);
INSERT INTO arbol_grupo VALUES (102, 38);
INSERT INTO arbol_grupo VALUES (102, 39);
INSERT INTO arbol_grupo VALUES (102, 40);
INSERT INTO arbol_grupo VALUES (102, 41);
INSERT INTO arbol_grupo VALUES (102, 42);
INSERT INTO arbol_grupo VALUES (102, 43);


--
-- TOC entry 2228 (class 0 OID 19831)
-- Dependencies: 173
-- Data for Name: area_investigacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO area_investigacion VALUES (6, 'area 1', true, 'matematica');


--
-- TOC entry 2229 (class 0 OID 19839)
-- Dependencies: 174
-- Data for Name: avance; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2230 (class 0 OID 19844)
-- Dependencies: 175
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2231 (class 0 OID 19852)
-- Dependencies: 176
-- Data for Name: comision; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2232 (class 0 OID 19857)
-- Dependencies: 177
-- Data for Name: condicion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO condicion VALUES (1, 'condicion1', true, 'condicion1');
INSERT INTO condicion VALUES (2, 'condicion2', true, 'condicion2');


--
-- TOC entry 2233 (class 0 OID 19865)
-- Dependencies: 178
-- Data for Name: condicion_programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO condicion_programa VALUES (2, 1, 1, 7);
INSERT INTO condicion_programa VALUES (0, 1, 8, NULL);
INSERT INTO condicion_programa VALUES (0, 2, 8, NULL);


--
-- TOC entry 2234 (class 0 OID 19870)
-- Dependencies: 179
-- Data for Name: cronograma; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO cronograma VALUES (true, '2013-12-26 00:00:00', '2014-02-12 00:00:00', 3, 1, 7);
INSERT INTO cronograma VALUES (true, '2013-12-27 00:00:00', '2013-12-25 00:00:00', 1, 1, 7);
INSERT INTO cronograma VALUES (true, '2013-12-25 00:00:00', '2013-12-18 00:00:00', 2, 1, 7);
INSERT INTO cronograma VALUES (true, '2013-12-25 00:00:00', '2013-12-10 00:00:00', 1, 2, 7);
INSERT INTO cronograma VALUES (true, '2013-12-27 00:00:00', '2013-12-25 00:00:00', 2, 2, 7);


--
-- TOC entry 2235 (class 0 OID 19875)
-- Dependencies: 180
-- Data for Name: defensa; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2236 (class 0 OID 19883)
-- Dependencies: 181
-- Data for Name: estudiante; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2237 (class 0 OID 19891)
-- Dependencies: 182
-- Data for Name: estudiante_teg; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2238 (class 0 OID 19896)
-- Dependencies: 183
-- Data for Name: etapa; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2239 (class 0 OID 19904)
-- Dependencies: 184
-- Data for Name: evaluacion_factibilidad; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2240 (class 0 OID 19909)
-- Dependencies: 185
-- Data for Name: factibilidad; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2259 (class 0 OID 20330)
-- Dependencies: 204
-- Data for Name: grupo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO grupo VALUES (101, true, 'ROLE_USER');
INSERT INTO grupo VALUES (102, true, 'ROLE_ADMIN');


--
-- TOC entry 2260 (class 0 OID 20333)
-- Dependencies: 205
-- Data for Name: grupo_usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO grupo_usuario VALUES (100, 101);
INSERT INTO grupo_usuario VALUES (112, 102);


--
-- TOC entry 2272 (class 0 OID 0)
-- Dependencies: 203
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 15, true);


--
-- TOC entry 2241 (class 0 OID 19927)
-- Dependencies: 186
-- Data for Name: item_defensa; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2242 (class 0 OID 19932)
-- Dependencies: 187
-- Data for Name: item_evaluacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO item_evaluacion VALUES (3, 'item 1', true, 'planteaminto', 'valor');
INSERT INTO item_evaluacion VALUES (4, 'item 2', true, 'objetivo', 'valor');
INSERT INTO item_evaluacion VALUES (5, 'item3', true, 'antecedentes', 'valor');
INSERT INTO item_evaluacion VALUES (14, 'otro item', true, 'item8', 'valor');


--
-- TOC entry 2243 (class 0 OID 19940)
-- Dependencies: 188
-- Data for Name: jurado; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2244 (class 0 OID 19945)
-- Dependencies: 189
-- Data for Name: lapso; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO lapso VALUES (7, true, '2013-12-26 00:00:00', '2013-12-05 00:00:00', '2013-1');
INSERT INTO lapso VALUES (15, true, '2016-07-15 00:00:00', '2013-12-27 00:00:00', '2014');


--
-- TOC entry 2245 (class 0 OID 19950)
-- Dependencies: 190
-- Data for Name: profesor; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2246 (class 0 OID 19958)
-- Dependencies: 191
-- Data for Name: profesor_area; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2247 (class 0 OID 19963)
-- Dependencies: 192
-- Data for Name: programa; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO programa VALUES (1, 'programa1', true, 'informatica');
INSERT INTO programa VALUES (2, 'programa2', true, 'produccion');
INSERT INTO programa VALUES (8, 'ingles', true, 'ingles');


--
-- TOC entry 2248 (class 0 OID 19971)
-- Dependencies: 193
-- Data for Name: programa_area; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO programa_area VALUES (6, 1, 7);


--
-- TOC entry 2249 (class 0 OID 19976)
-- Dependencies: 194
-- Data for Name: programa_item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO programa_item VALUES (3, 1, 7);


--
-- TOC entry 2225 (class 0 OID 19294)
-- Dependencies: 170
-- Data for Name: programa_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2250 (class 0 OID 19981)
-- Dependencies: 195
-- Data for Name: requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO requisito VALUES (9, 'requisito', true, 'requisito5');


--
-- TOC entry 2251 (class 0 OID 19989)
-- Dependencies: 196
-- Data for Name: solicitud_tutoria; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2252 (class 0 OID 19997)
-- Dependencies: 197
-- Data for Name: solicitud_tutoria_estudiante; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2253 (class 0 OID 20002)
-- Dependencies: 198
-- Data for Name: teg; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2254 (class 0 OID 20010)
-- Dependencies: 199
-- Data for Name: teg_etapa; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2255 (class 0 OID 20015)
-- Dependencies: 200
-- Data for Name: teg_requisito; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2256 (class 0 OID 20020)
-- Dependencies: 201
-- Data for Name: tematica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2257 (class 0 OID 20028)
-- Dependencies: 202
-- Data for Name: tipo_jurado; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2261 (class 0 OID 20336)
-- Dependencies: 206
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario VALUES (112, true, 'ysol', '$2a$10$2ShZ2y.Els2iizZJvufUYO7uUh6499bK6NXoOYb.CXFelvJ8MHpMS');
INSERT INTO usuario VALUES (100, true, 'willi', '$2a$10$sxSyH2JPrTtqXVffHFQnnOrxVNriHrG8DnlYf/ZN7LNifovQ2MoIC');
INSERT INTO usuario VALUES (6, true, 'cindy', '$2a$10$ZQ7P6OO/UexmfNCWoSxaAuiaaD4CUKG8TIgUKkt4BnXXuUvcH9Kz.');
INSERT INTO usuario VALUES (7, true, 'ramon', '$2a$10$ySesxrhAy6Q9KKcWVOr2zu8g3RZmExT0xNVHMsRSvhqOEelZ/uFXy');


--
-- TOC entry 1992 (class 2606 OID 19812)
-- Name: actividad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY actividad
    ADD CONSTRAINT actividad_pkey PRIMARY KEY (id);


--
-- TOC entry 1994 (class 2606 OID 19817)
-- Name: actividad_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT actividad_requisito_pkey PRIMARY KEY (actividad_id, requisito_id);


--
-- TOC entry 2064 (class 2606 OID 20429)
-- Name: arbol_grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT arbol_grupo_pkey PRIMARY KEY (grupo_id, arbol_id);


--
-- TOC entry 2062 (class 2606 OID 20431)
-- Name: arbol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY arbol
    ADD CONSTRAINT arbol_pkey PRIMARY KEY (id);


--
-- TOC entry 1996 (class 2606 OID 19838)
-- Name: area_investigacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY area_investigacion
    ADD CONSTRAINT area_investigacion_pkey PRIMARY KEY (id);


--
-- TOC entry 1998 (class 2606 OID 19843)
-- Name: avance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY avance
    ADD CONSTRAINT avance_pkey PRIMARY KEY (id);


--
-- TOC entry 2000 (class 2606 OID 19851)
-- Name: categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id);


--
-- TOC entry 2002 (class 2606 OID 19856)
-- Name: comision_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT comision_pkey PRIMARY KEY (teg_id, profesor_cedula);


--
-- TOC entry 2004 (class 2606 OID 19864)
-- Name: condicion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY condicion
    ADD CONSTRAINT condicion_pkey PRIMARY KEY (id);


--
-- TOC entry 2006 (class 2606 OID 19869)
-- Name: condicion_programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT condicion_programa_pkey PRIMARY KEY (condicion_id, programa_id);


--
-- TOC entry 2008 (class 2606 OID 19874)
-- Name: cronograma_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT cronograma_pkey PRIMARY KEY (actividad_id, programa_id);


--
-- TOC entry 2010 (class 2606 OID 19882)
-- Name: defensa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT defensa_pkey PRIMARY KEY (id);


--
-- TOC entry 2012 (class 2606 OID 19890)
-- Name: estudiante_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estudiante
    ADD CONSTRAINT estudiante_pkey PRIMARY KEY (cedula);


--
-- TOC entry 2014 (class 2606 OID 19895)
-- Name: estudiante_teg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT estudiante_teg_pkey PRIMARY KEY (teg_id, estudiante_cedula);


--
-- TOC entry 2016 (class 2606 OID 19903)
-- Name: etapa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY etapa
    ADD CONSTRAINT etapa_pkey PRIMARY KEY (id);


--
-- TOC entry 2018 (class 2606 OID 19908)
-- Name: evaluacion_factibilidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY evaluacion_factibilidad
    ADD CONSTRAINT evaluacion_factibilidad_pkey PRIMARY KEY (id);


--
-- TOC entry 2020 (class 2606 OID 19916)
-- Name: factibilidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT factibilidad_pkey PRIMARY KEY (id);


--
-- TOC entry 2056 (class 2606 OID 20343)
-- Name: grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupo
    ADD CONSTRAINT grupo_pkey PRIMARY KEY (id);


--
-- TOC entry 2058 (class 2606 OID 20345)
-- Name: grupo_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT grupo_usuario_pkey PRIMARY KEY (usuario_id, grupo_id);


--
-- TOC entry 2022 (class 2606 OID 19931)
-- Name: item_defensa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT item_defensa_pkey PRIMARY KEY (defensa_id, item_id);


--
-- TOC entry 2024 (class 2606 OID 19939)
-- Name: item_evaluacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_evaluacion
    ADD CONSTRAINT item_evaluacion_pkey PRIMARY KEY (id);


--
-- TOC entry 2026 (class 2606 OID 19944)
-- Name: jurado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT jurado_pkey PRIMARY KEY (factibilidad_id, item_evaluacion_id);


--
-- TOC entry 2028 (class 2606 OID 19949)
-- Name: lapso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY lapso
    ADD CONSTRAINT lapso_pkey PRIMARY KEY (id);


--
-- TOC entry 2032 (class 2606 OID 19962)
-- Name: profesor_area_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT profesor_area_pkey PRIMARY KEY (profesor_cedula, area_id);


--
-- TOC entry 2030 (class 2606 OID 19957)
-- Name: profesor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY profesor
    ADD CONSTRAINT profesor_pkey PRIMARY KEY (cedula);


--
-- TOC entry 2036 (class 2606 OID 19975)
-- Name: programa_area_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT programa_area_pkey PRIMARY KEY (area_investigacion_id, programa_id);


--
-- TOC entry 2038 (class 2606 OID 19980)
-- Name: programa_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT programa_item_pkey PRIMARY KEY (item_evaluacion_id, programa_id);


--
-- TOC entry 2034 (class 2606 OID 19970)
-- Name: programa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa
    ADD CONSTRAINT programa_pkey PRIMARY KEY (id);


--
-- TOC entry 1990 (class 2606 OID 19298)
-- Name: programa_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT programa_requisito_pkey PRIMARY KEY (programa_id, requisito_id);


--
-- TOC entry 2040 (class 2606 OID 19988)
-- Name: requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY requisito
    ADD CONSTRAINT requisito_pkey PRIMARY KEY (id);


--
-- TOC entry 2044 (class 2606 OID 20001)
-- Name: solicitud_tutoria_estudiante_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT solicitud_tutoria_estudiante_pkey PRIMARY KEY (solicitud_id, estudiante_cedula);


--
-- TOC entry 2042 (class 2606 OID 19996)
-- Name: solicitud_tutoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT solicitud_tutoria_pkey PRIMARY KEY (id);


--
-- TOC entry 2048 (class 2606 OID 20014)
-- Name: teg_etapa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT teg_etapa_pkey PRIMARY KEY (etapa_id, teg_id);


--
-- TOC entry 2046 (class 2606 OID 20009)
-- Name: teg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT teg_pkey PRIMARY KEY (id);


--
-- TOC entry 2050 (class 2606 OID 20019)
-- Name: teg_requisito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT teg_requisito_pkey PRIMARY KEY (requisito_id, teg_id);


--
-- TOC entry 2052 (class 2606 OID 20027)
-- Name: tematica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tematica
    ADD CONSTRAINT tematica_pkey PRIMARY KEY (id);


--
-- TOC entry 2054 (class 2606 OID 20035)
-- Name: tipo_jurado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_jurado
    ADD CONSTRAINT tipo_jurado_pkey PRIMARY KEY (id);


--
-- TOC entry 2060 (class 2606 OID 20347)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2109 (class 2606 OID 20269)
-- Name: fk1b5cb59a49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT fk1b5cb59a49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2110 (class 2606 OID 20274)
-- Name: fk1b5cb59a76d87132; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_etapa
    ADD CONSTRAINT fk1b5cb59a76d87132 FOREIGN KEY (etapa_id) REFERENCES etapa(id);


--
-- TOC entry 2107 (class 2606 OID 20259)
-- Name: fk1c01681f0a162; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT fk1c01681f0a162 FOREIGN KEY (tematica_id) REFERENCES tematica(id);


--
-- TOC entry 2108 (class 2606 OID 20264)
-- Name: fk1c016a4f276e3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg
    ADD CONSTRAINT fk1c016a4f276e3 FOREIGN KEY (tutor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2115 (class 2606 OID 20348)
-- Name: fk1ecb2d584241c752; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT fk1ecb2d584241c752 FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2114 (class 2606 OID 20353)
-- Name: fk1ecb2d58ff112f72; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY grupo_usuario
    ADD CONSTRAINT fk1ecb2d58ff112f72 FOREIGN KEY (grupo_id) REFERENCES grupo(id);


--
-- TOC entry 2070 (class 2606 OID 20054)
-- Name: fk228a90817b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a90817b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2069 (class 2606 OID 20049)
-- Name: fk228a9081857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a9081857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2068 (class 2606 OID 20044)
-- Name: fk228a9081d101c2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY actividad_requisito
    ADD CONSTRAINT fk228a9081d101c2f2 FOREIGN KEY (actividad_id) REFERENCES actividad(id);


--
-- TOC entry 2099 (class 2606 OID 20219)
-- Name: fk32e5c92f7b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92f7b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2097 (class 2606 OID 20209)
-- Name: fk32e5c92fb5e71917; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92fb5e71917 FOREIGN KEY (area_investigacion_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2098 (class 2606 OID 20214)
-- Name: fk32e5c92fcaebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_area
    ADD CONSTRAINT fk32e5c92fcaebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2102 (class 2606 OID 20234)
-- Name: fk32e973b57b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b57b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2101 (class 2606 OID 20229)
-- Name: fk32e973b5caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b5caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2100 (class 2606 OID 20224)
-- Name: fk32e973b5ec331a89; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_item
    ADD CONSTRAINT fk32e973b5ec331a89 FOREIGN KEY (item_evaluacion_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2103 (class 2606 OID 20239)
-- Name: fk56ac30334638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT fk56ac30334638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2104 (class 2606 OID 20244)
-- Name: fk56ac303381f0a162; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria
    ADD CONSTRAINT fk56ac303381f0a162 FOREIGN KEY (tematica_id) REFERENCES tematica(id);


--
-- TOC entry 2065 (class 2606 OID 20307)
-- Name: fk5788eaa17b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa17b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2066 (class 2606 OID 20302)
-- Name: fk5788eaa1857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa1857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2067 (class 2606 OID 20297)
-- Name: fk5788eaa1caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY programa_requisito
    ADD CONSTRAINT fk5788eaa1caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2080 (class 2606 OID 20114)
-- Name: fk5c158e3c4638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT fk5c158e3c4638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2081 (class 2606 OID 20119)
-- Name: fk5c158e3c49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY defensa
    ADD CONSTRAINT fk5c158e3c49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2076 (class 2606 OID 20094)
-- Name: fk5d6696707b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d6696707b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2074 (class 2606 OID 20084)
-- Name: fk5d66967081fb8492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d66967081fb8492 FOREIGN KEY (condicion_id) REFERENCES condicion(id);


--
-- TOC entry 2075 (class 2606 OID 20089)
-- Name: fk5d669670caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condicion_programa
    ADD CONSTRAINT fk5d669670caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2079 (class 2606 OID 20109)
-- Name: fk7bac5e97b0dd4b2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e97b0dd4b2 FOREIGN KEY (lapso_id) REFERENCES lapso(id);


--
-- TOC entry 2078 (class 2606 OID 20104)
-- Name: fk7bac5e9caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e9caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2077 (class 2606 OID 20099)
-- Name: fk7bac5e9d101c2f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cronograma
    ADD CONSTRAINT fk7bac5e9d101c2f2 FOREIGN KEY (actividad_id) REFERENCES actividad(id);


--
-- TOC entry 2096 (class 2606 OID 20204)
-- Name: fk8bc6b5c24638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT fk8bc6b5c24638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2095 (class 2606 OID 20199)
-- Name: fk8bc6b5c266e3cd8b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor_area
    ADD CONSTRAINT fk8bc6b5c266e3cd8b FOREIGN KEY (area_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2106 (class 2606 OID 20254)
-- Name: fk9ab055eb8fe1ec8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT fk9ab055eb8fe1ec8 FOREIGN KEY (solicitud_id) REFERENCES solicitud_tutoria(id);


--
-- TOC entry 2105 (class 2606 OID 20249)
-- Name: fk9ab055ebf20a6af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY solicitud_tutoria_estudiante
    ADD CONSTRAINT fk9ab055ebf20a6af FOREIGN KEY (estudiante_cedula) REFERENCES estudiante(cedula);


--
-- TOC entry 2087 (class 2606 OID 20159)
-- Name: fka3fed108be4492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT fka3fed108be4492 FOREIGN KEY (defensa_id) REFERENCES defensa(id);


--
-- TOC entry 2088 (class 2606 OID 20164)
-- Name: fka3fed10bdeb82cf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_defensa
    ADD CONSTRAINT fka3fed10bdeb82cf FOREIGN KEY (item_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2071 (class 2606 OID 20069)
-- Name: fkac32ab0449ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avance
    ADD CONSTRAINT fkac32ab0449ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2113 (class 2606 OID 20289)
-- Name: fkb06b2ad8b5e71917; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tematica
    ADD CONSTRAINT fkb06b2ad8b5e71917 FOREIGN KEY (area_investigacion_id) REFERENCES area_investigacion(id);


--
-- TOC entry 2085 (class 2606 OID 20139)
-- Name: fkb7a6c2e64638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT fkb7a6c2e64638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2086 (class 2606 OID 20144)
-- Name: fkb7a6c2e649ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY factibilidad
    ADD CONSTRAINT fkb7a6c2e649ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2092 (class 2606 OID 20184)
-- Name: fkbb87bac54638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac54638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2089 (class 2606 OID 20169)
-- Name: fkbb87bac549ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac549ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2090 (class 2606 OID 20174)
-- Name: fkbb87bac579a06aa2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac579a06aa2 FOREIGN KEY (factibilidad_id) REFERENCES factibilidad(id);


--
-- TOC entry 2093 (class 2606 OID 20189)
-- Name: fkbb87bac5cafdd94b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac5cafdd94b FOREIGN KEY (tipo_jurado_id) REFERENCES tipo_jurado(id);


--
-- TOC entry 2091 (class 2606 OID 20179)
-- Name: fkbb87bac5ec331a89; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY jurado
    ADD CONSTRAINT fkbb87bac5ec331a89 FOREIGN KEY (item_evaluacion_id) REFERENCES item_evaluacion(id);


--
-- TOC entry 2094 (class 2606 OID 20194)
-- Name: fkc440f5eaf6339732; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY profesor
    ADD CONSTRAINT fkc440f5eaf6339732 FOREIGN KEY (categoria_id) REFERENCES categoria(id);


--
-- TOC entry 2111 (class 2606 OID 20279)
-- Name: fkd54ee51a49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT fkd54ee51a49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2112 (class 2606 OID 20284)
-- Name: fkd54ee51a857aa532; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY teg_requisito
    ADD CONSTRAINT fkd54ee51a857aa532 FOREIGN KEY (requisito_id) REFERENCES requisito(id);


--
-- TOC entry 2072 (class 2606 OID 20074)
-- Name: fkdbe5c9bd4638e5af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT fkdbe5c9bd4638e5af FOREIGN KEY (profesor_cedula) REFERENCES profesor(cedula);


--
-- TOC entry 2073 (class 2606 OID 20079)
-- Name: fkdbe5c9bd49ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comision
    ADD CONSTRAINT fkdbe5c9bd49ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2084 (class 2606 OID 20134)
-- Name: fkea10a42949ebaa52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT fkea10a42949ebaa52 FOREIGN KEY (teg_id) REFERENCES teg(id);


--
-- TOC entry 2083 (class 2606 OID 20129)
-- Name: fkea10a429bf20a6af; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante_teg
    ADD CONSTRAINT fkea10a429bf20a6af FOREIGN KEY (estudiante_cedula) REFERENCES estudiante(cedula);


--
-- TOC entry 2082 (class 2606 OID 20124)
-- Name: fkeb92edd2caebb742; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY estudiante
    ADD CONSTRAINT fkeb92edd2caebb742 FOREIGN KEY (programa_id) REFERENCES programa(id);


--
-- TOC entry 2117 (class 2606 OID 20432)
-- Name: fkf94f7cd86ef9c552; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT fkf94f7cd86ef9c552 FOREIGN KEY (arbol_id) REFERENCES arbol(id);


--
-- TOC entry 2116 (class 2606 OID 20437)
-- Name: fkf94f7cd8ff112f72; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY arbol_grupo
    ADD CONSTRAINT fkf94f7cd8ff112f72 FOREIGN KEY (grupo_id) REFERENCES grupo(id);


--
-- TOC entry 2270 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-12-17 15:55:55

--
-- PostgreSQL database dump complete
--

