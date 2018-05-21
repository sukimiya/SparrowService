/*
 * Project:sparrow nest
 * LastModified:18-4-11 下午10:55 by lily
 *
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.e2x.sparrow.crud.mongod.repo.security;

import io.e2x.sparrow.crud.mongod.vo.security.OAuthClientDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableReactiveMongoRepositories
public interface OAuthClientRepository extends ReactiveMongoRepository<OAuthClientDetail,String>{
    Mono<OAuthClientDetail> findByClientId(String s);
    Flux<OAuthClientDetail> findAllByClientIdLike(String s);
    Flux<OAuthClientDetail> findAllByDomainLike(String s);
}
