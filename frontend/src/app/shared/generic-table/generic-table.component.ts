import {
    AfterViewChecked,
    Component,
    computed,
    effect,
    inject,
    input,
    PipeTransform,
    signal
} from '@angular/core';
import {
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell, MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef, MatNoDataRow,
    MatRow, MatRowDef, MatTable, MatTableDataSource
} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatSortHeader} from "@angular/material/sort";
import {ScopedTranslationPipe} from "../pipes/scoped-translation-pipe";
import {CaseFormatter} from "../format/case-formatter";

interface GenericTableColumn<T> {
    field: keyof T;
    getValue?: (model: T) => T[keyof T];
    pipes?: PipeTransform[];
}

@Component({
  selector: 'app-generic-table',
    imports: [
        MatCell,
        MatCellDef,
        MatColumnDef,
        MatHeaderCell,
        MatHeaderRow,
        MatHeaderRowDef,
        MatRow,
        MatRowDef,
        MatSort,
        MatSortHeader,
        MatTable,
        ScopedTranslationPipe,
        MatHeaderCellDef,
        MatNoDataRow
    ],
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.scss'
})
export class GenericTableComponent<T extends object> implements AfterViewChecked{
    caseFormatter = inject(CaseFormatter)

    idAttr = input.required<keyof T>()
    dataSource = input.required<MatTableDataSource<T>>()
    entries = signal<T[]>([])
    tableDefDefault = computed(()=> this.createDefaultTableDef(this.entries()?.[0]))
    columns = computed(()=>this.tableDefDefault().map(e => e.field.toString()))

    ngAfterViewChecked(): void {
        this.entries.set(this.dataSource().data)
    }

    private createDefaultTableDef<T extends object>(model:T): GenericTableColumn<T>[]{
        if (!model){
            return []
        }
        return this.objectKeys(model)
            .map((field) => ({ field } as GenericTableColumn<T>))
            .map(e => this.enrichGenericColumn(e));
    }

    private objectKeys<T extends object>(obj: T): (keyof T)[] {
        return Object.keys(obj) as (keyof T)[];
    }

    private enrichGenericColumn<T>(def: GenericTableColumn<T>): GenericTableColumn<T> {
        return {
            ...def,
            getValue: def.getValue ?? ((o:T)=>o[def.field]),
            pipes: def.pipes ?? []
        };
    }
}
