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

import com.finch.api.data.vo.IngredientVO;
import com.finch.api.services.IIngredientService;
import com.finch.api.util.HateoasUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "IngredientEndpoint")
@RestController
@RequestMapping("/api/ingredient")
public class IngredientController {

	@Autowired
	private IIngredientService service;

    @Autowired
    private PagedResourcesAssembler<IngredientVO> assembler;

    private static final String DESC_CONST = "desc";

	@ApiOperation(value = "Return a simple String of test of version API Response defined by header: X-API-VERSION" )
	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=2")
	public String findAll() {
		return "Hello API V2";
	}

    @ApiOperation(value = "List all Ingredients" )
    @GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<PagedModel<EntityModel<IngredientVO>>> findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {
    	Page<IngredientVO> ingredients =  null;
    	PagedModel<EntityModel<IngredientVO>> resources = null;
    	Pageable pageable = null;
    	Direction sortDirection = null;
    	
        sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        ingredients =  service.findAll(pageable);
        ingredients
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(IngredientController.class).findById(p.getKey())).withSelfRel(),
                        linkTo(methodOn(IngredientController.class).update(p)).withRel(HateoasUtil.UPDATE_REL),
                        linkTo(methodOn(IngredientController.class).delete(p.getKey())).withRel(HateoasUtil.DELETE_REL)
                        )
                );

        resources = assembler.toModel(ingredients);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find Ingredients by name" )
    @GetMapping(value = "/findIngredientByName/{name}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
    public ResponseEntity<PagedModel<EntityModel<IngredientVO>>> findPersonByName(
            @PathVariable("name") String name,
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction) {
    	Page<IngredientVO> ingredients =  null;
    	PagedModel<EntityModel<IngredientVO>> resources = null;
    	Pageable pageable = null;
    	Direction sortDirection = null;
    	
    	sortDirection = DESC_CONST.equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

    	pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

    	ingredients =  service.findIngredientByName(name, pageable);
    	ingredients
                .stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(IngredientController.class).findById(p.getKey())).withSelfRel(),
                        linkTo(methodOn(IngredientController.class).update(p)).withRel(HateoasUtil.UPDATE_REL),
                        linkTo(methodOn(IngredientController.class).delete(p.getKey())).withRel(HateoasUtil.DELETE_REL)
                        )
                );

        resources = assembler.toModel(ingredients);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find a Ingredient by ID" )
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public IngredientVO findById(@PathVariable("id") Long id) {
    	IngredientVO ingredientVO = null;
    	ingredientVO = service.findById(id);
    	ingredientVO.add(linkTo(methodOn(IngredientController.class).findById(id)).withSelfRel());
    	ingredientVO.add(linkTo(methodOn(IngredientController.class).update(ingredientVO)).withRel(HateoasUtil.UPDATE_REL));
    	ingredientVO.add(linkTo(methodOn(IngredientController.class).delete(id)).withRel(HateoasUtil.DELETE_REL));
		return ingredientVO;
	}

	@ApiOperation(value = "Create a new Ingredient")
	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public IngredientVO create(@RequestBody IngredientVO ingredient) {
		IngredientVO ingredientVO = service.create(ingredient);
		ingredientVO.add(linkTo(methodOn(IngredientController.class).findById(ingredientVO.getKey())).withSelfRel());
		ingredientVO.add(linkTo(methodOn(IngredientController.class).update(ingredientVO)).withRel(HateoasUtil.UPDATE_REL));
		ingredientVO.add(linkTo(methodOn(IngredientController.class).delete(ingredientVO.getKey())).withRel(HateoasUtil.DELETE_REL));
		return ingredientVO;
	}

	@ApiOperation(value = "Update a Ingredient by ID")
	@PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
			consumes = { "application/json", "application/xml", "application/x-yaml" }, headers = "X-API-VERSION=1")
	public IngredientVO update(@RequestBody IngredientVO ingredient) {
		IngredientVO ingredientVO = service.update(ingredient);
		ingredientVO.add(linkTo(methodOn(IngredientController.class).findById(ingredientVO.getKey())).withSelfRel());
		ingredientVO.add(linkTo(methodOn(IngredientController.class).update(ingredientVO)).withRel(HateoasUtil.UPDATE_REL));
		return ingredientVO;
	}

	@ApiOperation(value = "Delete a Ingredient by ID")
	@DeleteMapping(value="/{id}", headers = "X-API-VERSION=1")
	public ResponseEntity<BodyBuilder> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

}
