package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.vo.AlumnosListVO;

public interface AlumnosDAO extends JpaRepository<Alumnos, Integer> {

	@Query("update Alumnos a set a.alumnoActivo = ?1 where a.alumnoId = ?2 ")
	public void changeActivo(boolean estatusId, Integer alumnoId);
/*	
	select al.ALUMNO_ID, al.ALUMNO_NOMBRE, al.ALUMNO_AP_PATERNO, al.ALUMNO_AP_MATERNO,
	al.ALUMNO_TUTOR, al.ALUMNO_TELEFONO1, al.ALUMNO_IMAGEN, curso.CURSO_NOMBRE
	from alumnos al
	join alumno_curso ac on al.ALUMNO_ID = ac.ALUMNO_ID
	join cursos curso on ac.CURSO_ID = curso.CURSO_ID
	where ac.ALUMNOCURSO_ID = 
		(select min(ac2.alumnocurso_id) from alumno_curso ac2 
			join cursos curso2 on ac2.curso_id = curso2.curso_id 
        	where ac2.alumno_id = al.alumno_id 
        	and curso2.CURSO_ESTATUS= 2);
*/
	
	@Query("Select new com.sae.gandhi.spring.vo.AlumnosListVO(al.alumnoId, al.alumnoNombre, "
			+ "al.alumnoApPaterno, al.alumnoApMaterno, al.alumnoTutor, al.alumnoTutorTelefono1, "
			+ "al.alumnoImagen) "
			+ "from Alumnos al ")
//			+ "join ac.alumno al "
//			+ "join ac.curso curso "
//			+ "where ac.alumnoCursoId = "
			//Esta sección es para obtener el 1er curso activo al cual se inscribió al alumno
			//Esta pensado que siempre se inscribe primero a un grado y posteriormente a 
			//cursos extracurriculares
//			+ "	(select min(ac2.alumnoCursoId) from AlumnoCurso ac2 "
//			+ "		join ac2.curso curso2 "
//			+ "		where ac2.alumnoId = al.alumnoId"
//			+ "		and curso2.cursoStatus = 2)")
	public List<AlumnosListVO> getAlumnosList();
}
