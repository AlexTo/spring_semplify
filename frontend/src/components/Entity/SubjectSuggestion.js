import {TextField} from "@material-ui/core";
import {Autocomplete} from "@material-ui/lab";
import React, {Fragment, useEffect, useState} from "react";
import {CircularProgress, Typography, Grid, Avatar} from "@material-ui/core";
import {useLazyQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../queries/indexerQueries";
import {useDebounce} from "../../hooks";

const renderOption = (option) => <Grid container spacing={1}>
  <Grid item>
    {option.thumbnailUri && <Avatar alt={option.prefLabel} src={option.thumbnailUri}/>}
  </Grid>
  <Grid item xs>
    {option.uri}
    <Typography variant="body2" color="textSecondary">
      {option.prefLabel}
    </Typography>
  </Grid>
</Grid>;

function SubjectSuggestion({label, onOptionSelected}) {

  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const [load, {called, loading, data}] = useLazyQuery(indexerQueries.suggest);


  useEffect(() => {
    if (debouncedSearchTerm) {

      if (debouncedSearchTerm.length < 3) return;

      load({
        variables: {
          "query": {
            "text": debouncedSearchTerm,
            "type": "subject"
          }
        }
      })
    }
  }, [debouncedSearchTerm]);

  useEffect(() => {
    if (!open) {
      setOptions([]);
      return;
    }
    if (data && data.suggest) {
      setOptions(data.suggest.suggestions);
    }
  }, [open, data]);

  return (
    <Autocomplete
      open={open}
      onOpen={() => {
        setOpen(true);
      }}
      onClose={() => {
        setOpen(false);
      }}
      onChange={(event, value) => onOptionSelected(value)}
      getOptionLabel={() => ""}
      options={options}
      loading={called && loading}
      renderOption={renderOption}
      renderInput={params => (
        <TextField
          {...params}
          label={label}
          variant="outlined"
          onChange={e => setSearchTerm(e.target.value)}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <Fragment>
                {loading ? <CircularProgress color="inherit" size={20}/> : null}
                {params.InputProps.endAdornment}
              </Fragment>
            ),
            autoComplete: 'new-password',
          }}
        />)}/>
  )
}

export default SubjectSuggestion;
