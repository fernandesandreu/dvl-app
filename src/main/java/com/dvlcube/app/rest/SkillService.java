package com.dvlcube.app.rest;

import static com.dvlcube.app.manager.data.e.Menu.CONFIGURATION;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvlcube.app.interfaces.MenuItem;
import com.dvlcube.app.jpa.repo.SkillRepository;
import com.dvlcube.app.manager.data.SkillBean;
import com.dvlcube.app.manager.data.vo.MxRestResponse;
import com.dvlcube.utils.interfaces.rest.MxFilterableBeanService;

/**
 * @since 4 de jun de 2019
 * @author Ulisses Lima
 */
@RestController
@MenuItem(value = CONFIGURATION)
@RequestMapping("${dvl.rest.prefix}/skills")
public class SkillService implements MxFilterableBeanService<SkillBean, Long> {

	@Autowired
	private SkillRepository repo;

	@Override
	@GetMapping
	public Iterable<SkillBean> get(@RequestParam Map<String, String> params) {
		return repo.firstPage().stream().sorted((i1, i2) -> i1.getName().compareTo(i2.getName())).collect(Collectors.toList());
	}

	@Override
	@GetMapping("/{id}")
	public Optional<SkillBean> get(@PathVariable Long id) {
		return repo.findById(id);
	}

	@Override
	@PostMapping
	public MxRestResponse post(@Valid @RequestBody SkillBean body) {
		SkillBean save = repo.save(body);
		return GenericRestResponse.ok(save.getId());
	}

	/**
	 * @param params
	 * @return List<SkillBean>
	 * @since 18 de abr de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping("/filtered")
	public List<SkillBean> getFiltered(@RequestParam Map<String, String> params) {
		return repo.findAllBy(params);
	}

	/**
	 * @param group
	 * @param params
	 * @return List<SkillBean>
	 * @since 18 de abr de 2019
	 * @author Ulisses Lima
	 */
	@GetMapping("/group/{group}/filtered")
	public List<SkillBean> getGroupFiltered(@PathVariable String group, @RequestParam Map<String, String> params) {
		return repo.findAllBy(params, group);
	}

	@GetMapping("/like/{name}")
	public Iterable<SkillBean> getLike(@PathVariable String name) {
		return repo.findAllLike(name);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		repo.deleteById(id);
	}
	
	@GetMapping("/name/{name}")
	public SkillBean getByName(@PathVariable String name) {
		return repo.findByName(name);
	}
	
	@GetMapping("/exist/name/{name}")
	public String getExistByName(@PathVariable String name) {
		
		SkillBean sb = repo.findByName(name);
		
		return Objects.isNull(sb) ? "Not Exist":"Exist";
	}
	
}
