package com.finch.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finch.api.data.vo.SnackVO;
import com.finch.api.services.ISnackService;
import com.finch.api.util.HateoasUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "SnackEndpoint")
@RestController
@RequestMapping("/api/snack")
public class SnackController {

	@Autowired
	private ISnackService service;

    @Autowired
    private PagedResourcesAssembler<SnackVO> assembler;

    private static final String DESC_CONST = "desc";

	@ApiOperation(value = "Return a simple String of test of version API Response defined by header: X-API-VERSION" )
	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=2")
	public String findAll() {
		return "Hello API V2";
	}

    @ApiOperation(value = "List all Snacks" )
    @GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<PagedModel<EntityModel<SnackVO>>> findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {
    	Page<SnackVO> snacks =  null;
    	PagedModel<EntityModel<SnackVO>> resources = null;
    	Pageable pageable = null;
    	Direction sortDirection = null;
    	
        sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        snacks =  service.findAll(pageable);
        snacks
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(SnackController.class).findById(p.getKey())).withSelfRel(),
                        linkTo(methodOn(SnackController.class).update(p)).withRel(HateoasUtil.UPDATE_REL),
                        linkTo(methodOn(SnackController.class).delete(p.getKey())).withRel(HateoasUtil.DELETE_REL)
                        )
                );

        resources = assembler.toModel(snacks);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Snacks by name" )
    @GetMapping(value = "/findSnackByName/{name}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<PagedModel<EntityModel<SnackVO>>> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {
    	Page<SnackVO> snacks =  null;
    	PagedModel<EntityModel<SnackVO>> resources = null;
    	Pageable pageable = null;
    	Direction sortDirection = null;
    	
    	sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

    	pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

    	snacks =  service.findSnackByName(name, pageable);
    	snacks
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(SnackController.class).findById(p.getKey())).withSelfRel(),
                        linkTo(methodOn(SnackController.class).update(p)).withRel(HateoasUtil.UPDATE_REL),
                        linkTo(methodOn(SnackController.class).delete(p.getKey())).withRel(HateoasUtil.DELETE_REL)
                        )
                );

        resources = assembler.toModel(snacks);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find a Snack by ID" )
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public SnackVO findById(@PathVariable("id") Long id) {
		SnackVO snackVO = null;
		snackVO = service.findById(id);
		snackVO.add(linkTo(methodOn(SnackController.class).findById(id)).withSelfRel());
		snackVO.add(linkTo(methodOn(SnackController.class).update(snackVO)).withRel(HateoasUtil.UPDATE_REL));
		snackVO.add(linkTo(methodOn(SnackController.class).delete(id)).withRel(HateoasUtil.DELETE_REL));
		return snackVO;
	}

	@ApiOperation(value = "Create a new Snack")
	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public SnackVO create(@RequestBody SnackVO location) {
		SnackVO snackVO = service.create(location);
		snackVO.add(linkTo(methodOn(SnackController.class).findById(snackVO.getKey())).withSelfRel());
		snackVO.add(linkTo(methodOn(SnackController.class).update(snackVO)).withRel(HateoasUtil.UPDATE_REL));
		snackVO.add(linkTo(methodOn(SnackController.class).delete(snackVO.getKey())).withRel(HateoasUtil.DELETE_REL));
		return snackVO;
	}

	@ApiOperation(value = "Update a Snack by ID")
	@PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public SnackVO update(@RequestBody SnackVO location) {
		SnackVO snackVO = service.update(location);
		snackVO.add(linkTo(methodOn(SnackController.class).findById(snackVO.getKey())).withSelfRel());
		snackVO.add(linkTo(methodOn(SnackController.class).update(snackVO)).withRel(HateoasUtil.UPDATE_REL));
		return snackVO;
	}

	@ApiOperation(value = "Delete a Snack by ID")
	@DeleteMapping(value="/{id}", headers = "X-API-VERSION=1")
	public ResponseEntity<BodyBuilder> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

}
