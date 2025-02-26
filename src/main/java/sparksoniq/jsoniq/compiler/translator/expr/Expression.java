/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.jsoniq.compiler.translator.expr;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.StaticContext;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public abstract class Expression extends ExpressionOrClause {

    protected StaticContext _staticContext;

    public Expression() {
        super();
    }

    public Expression(ExpressionMetadata metadata) {
        super(metadata);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitDescendants(this, argument);
    }

    public StaticContext getStaticContext() {
        return _staticContext;
    }

    public void setStaticContext(StaticContext _staticContext) {
        this._staticContext = _staticContext;
    }

    @Override
    public String serializationString(boolean prefix) {
        return "";
    }


}
